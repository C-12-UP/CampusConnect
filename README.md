# CampusConnect
CampusConnect — A Java Swing app where students can report and browse lost and found items on campus.

## Running (Windows)

Requirements: Python 3 and a JDK 11 or newer.

1. Start the Django site:

    cd web
    python -m venv venv
    .\venv\Scripts\Activate.ps1
    pip install -r requirements.txt
    python manage.py migrate
    python manage.py createsuperuser
    python manage.py runserver

2. In a second terminal, from the repo root, get the SQLite driver and run the app:

    mkdir lib
    Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.53.1.0/sqlite-jdbc-3.53.1.0.jar" -OutFile "lib\sqlite-jdbc-3.53.1.0.jar"
    javac -d out src\*.java
    xcopy src\Images out\Images /E /I /Y
    java -cp "out;src;lib\*" Main

The browser opens on the sign-in page. After signing in, the app opens.
Items are saved in `lostfound.db` in your user folder.
