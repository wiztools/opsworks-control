# OpsWorks Control

Command line tool to control AWS OpsWorks stacks and instances. Example usage:

```
$ java -jar opsworks-control-VER-jar-with-dependencies.jar \
  -a aws-access-key \
  -s aws-secret-key \
  -c start-instance \
  11111111-1111-1111-1111-instance1111 22222222-2222-2222-2222-instance2222 ...
```

To know about command line options:

```
$ java -jar opsworks-control-VER-jar-with-dependencies.jar -h
```

This tool is available as part of _"The Joy of Unix in Windows Tool Bundle"_:

[![](http://static.wiztools.org/wiztools-cli-tools.png)](http://cli-bundle.wiztools.org/)
