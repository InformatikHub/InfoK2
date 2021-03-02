Sollte es bei der Ausführung des Projekts zu folgender Fehlermeldung kommen:
java.lang.IllegalStateException: Toolkit not initialized
        at com.sun.javafx.application.PlatformImpl.runLater(PlatformImpl.java:273)
        at com.sun.javafx.application.PlatformImpl.runLater(PlatformImpl.java:268)
        at javafx.application.Platform.runLater(Platform.java:83)
        at bluej.runtime.ExecServer.runOnTargetThread(ExecServer.java:902)
        at bluej.runtime.ExecServer.access$700(ExecServer.java:78)
        at bluej.runtime.ExecServer$3.run(ExecServer.java:787)

dann lösen Sie das Problem, indem Sie entweder
a) die neuste BlueJ-Version installieren.
b) in BlueJ unter Werkzeuge->Einstellungen->Diverses->Run user code in this project on thread: "Default" wählen (BlueJ Version 4.1.2)
