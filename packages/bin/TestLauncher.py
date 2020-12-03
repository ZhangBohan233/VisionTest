if __name__ == "__main__":
    import os
    path1 = "DvaTest-jar-with-dependencies.jar"
    if os.path.exist(path1):
        path = path1
    else:
        path = "bin" + os.sep + path1
    os.system("javaw -jar " + path)
