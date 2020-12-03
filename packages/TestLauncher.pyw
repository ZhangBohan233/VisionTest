if __name__ == "__main__":
    import os
    import subprocess

    path1 = "DvaTest-jar-with-dependencies.jar"
    if os.path.exists(path1):
        path = path1
    else:
        path = "bin" + os.sep + path1
    arg = "javaw -jar " + path
    proc = subprocess.Popen(
        arg,
        shell=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        stdin=subprocess.PIPE
    )
    proc.stdin.close()
    proc.wait()
    proc.stdout.close()
