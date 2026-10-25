import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DesktopLogin {
    private static final String SITE = "http://127.0.0.1:8000"; // Django dev server
    private static final int TIMEOUT_SECONDS = 120;             // give up after 2 minutes
    private static final Pattern USERNAME = Pattern.compile("\"username\"\\s*:\\s*\"([^\"]+)\"");

    /** Runs the whole browser sign-in. Returns the username, or null on cancel/timeout/error. */
    public static String signIn() {
        CompletableFuture<String> codeFuture = new CompletableFuture<>(); // the callback fills this
        String state = randomState();
        JDialog dialog = buildDialog();
        HttpServer server = null;
        try {
            server = startListener(state, codeFuture);
            int port = server.getAddress().getPort(); // the free port the OS picked
            openBrowser(SITE + "/desktop-login/?port=" + port + "&state=" + state);

            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override protected String doInBackground() throws Exception {
                    String code = codeFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS); // waits off the UI thread
                    return exchange(code);
                }
                @Override protected void done() { dialog.dispose(); } // closes on success, timeout or error
            };
            dialog.addWindowListener(new WindowAdapter() {
                @Override public void windowOpened(WindowEvent e) { worker.execute(); }    // start once visible
                @Override public void windowClosed(WindowEvent e) { worker.cancel(true); } // Cancel / X (no-op if done)
            });
            dialog.setVisible(true); // modal: blocks this thread (not the Swing UI) until the dialog closes
            return worker.get();     // the username, or throws on cancel/timeout/error
        } catch (CancellationException e) {
            return null;             // user cancelled
        } catch (Exception e) {
            System.err.println("Sign-in failed: " + e); // timeout, bad status, no browser...
            return null;
        } finally {
            if (server != null) server.stop(1);          // always stop the listener
            SwingUtilities.invokeLater(dialog::dispose); // safety net: dialog closes on every outcome
        }
    }

    /** 24 random letters/digits from SecureRandom; the browser must echo it back unchanged. */
    private static String randomState() {
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 24; i++) sb.append(abc.charAt(rnd.nextInt(abc.length())));
        return sb.toString();
    }

    /** Starts a loopback-only server on a free port; /callback hands the code to codeFuture. */
    private static HttpServer startListener(String state, CompletableFuture<String> codeFuture) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        server.createContext("/callback", ex -> {
            Map<String, String> q = parseQuery(ex.getRequestURI().getRawQuery());
            if (!"GET".equals(ex.getRequestMethod()) || !state.equals(q.get("state")) || q.get("code") == null) {
                reply(ex, 400, "Bad request."); // wrong state: reject and ignore
                return;
            }
            reply(ex, 200, "<html><body><h3>Signed in. You can return to CampusConnect.</h3></body></html>");
            codeFuture.complete(q.get("code")); // wake the waiting thread (after the reply is sent)
        });
        server.start();
        return server;
    }

    /** Turns "a=1&b=2" into a map, URL-decoding both sides. */
    private static Map<String, String> parseQuery(String raw) {
        Map<String, String> m = new HashMap<>();
        if (raw == null) return m;
        for (String pair : raw.split("&")) {
            int i = pair.indexOf('=');
            if (i > 0) m.put(URLDecoder.decode(pair.substring(0, i), StandardCharsets.UTF_8),
                             URLDecoder.decode(pair.substring(i + 1), StandardCharsets.UTF_8));
        }
        return m;
    }

    /** Sends a small HTML reply with the given status and ends the exchange. */
    private static void reply(HttpExchange ex, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); } // closing finishes the exchange
    }

    /** Opens the default browser on the given URL. */
    private static void openBrowser(String url) throws Exception {
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
            throw new UnsupportedOperationException("This machine can't open a browser from Java.");
        Desktop.getDesktop().browse(new URI(url));
    }

    /** Small modal "Waiting..." window with a Cancel button. */
    private static JDialog buildDialog() {
        JDialog d = new JDialog((Frame) null, "Sign in", true);
        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // the X button also cancels
        ((JComponent) d.getContentPane()).setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> d.dispose());
        d.add(new JLabel("Waiting for browser sign-in...", SwingConstants.CENTER), BorderLayout.CENTER);
        d.add(cancel, BorderLayout.SOUTH);
        d.setSize(320, 140);
        d.setLocationRelativeTo(null); // centre on screen
        return d;
    }

    /** POSTs the code to Django and returns the username; throws if the server says no. */
    private static String exchange(String code) throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(SITE + "/api/desktop/exchange/"))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)))
                .build();
        HttpResponse<String> res = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 200) throw new IOException("Server answered " + res.statusCode());
        Matcher m = USERNAME.matcher(res.body());
        if (!m.find()) throw new IOException("No username in the reply");
        return m.group(1);
    }
}