import hashlib
import re
import secrets
from datetime import timedelta

from django.contrib.auth.decorators import login_required
from django.http import HttpResponse, HttpResponseRedirect, JsonResponse
from django.utils import timezone
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_POST

from .models import DesktopLoginCode

PORT_RE = re.compile(r"[0-9]{4,5}")              # digits only, so int() can't blow up
STATE_RE = re.compile(r"[A-Za-z0-9_-]{16,128}")  # allowed shape of `state`
CODE_LIFETIME = timedelta(minutes=2)             # how long a code stays valid


def _hash(code):
    """Return the SHA-256 hex digest (64 chars) of a code. Only this is stored."""
    return hashlib.sha256(code.encode()).hexdigest()


@login_required  # logged-out users go to the lamp page first, then come back here
def desktop_login(request):
    """Endpoint A: validate port/state, mint a one-time code, send it to the desktop app."""
    port = request.GET.get("port", "")
    state = request.GET.get("state", "")

    # Reject anything that isn't a port in 1024-65535 or a 16-128 char safe state.
    if not PORT_RE.fullmatch(port) or not 1024 <= int(port) <= 65535:
        return HttpResponse("Invalid port.", status=400, content_type="text/plain")
    if not STATE_RE.fullmatch(state):
        return HttpResponse("Invalid state.", status=400, content_type="text/plain")

    code = secrets.token_urlsafe(32)  # the secret; only the app ever sees it
    DesktopLoginCode.objects.create(user=request.user, code_hash=_hash(code))

    # Host is hard-coded: nothing from the request picks where we redirect (no open redirect).
    return HttpResponseRedirect(f"http://127.0.0.1:{int(port)}/callback?code={code}&state={state}")


@csrf_exempt  # the desktop client has no CSRF cookie
@require_POST
def desktop_exchange(request):
    """Endpoint B: trade a valid, unused, fresh code for the username (once only)."""
    code = request.POST.get("code", "")
    matches = DesktopLoginCode.objects.filter(
        code_hash=_hash(code),
        used=False,
        created_at__gte=timezone.now() - CODE_LIFETIME,
    )
    row = matches.select_related("user").first()

    # update() is one atomic SQL statement, so two simultaneous requests can't both win.
    if row is None or matches.filter(pk=row.pk).update(used=True) == 0:
        return JsonResponse({"ok": False}, status=401)
    return JsonResponse({"ok": True, "username": row.user.username})