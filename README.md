# request-baskets-qa [![Build Status](https://travis-ci.com/laker2000/request-baskets-qa.svg?branch=master)](https://travis-ci.com/laker2000/request-baskets-qa)
                              

## Install


Prerequisite
```prerequisite
$ install golang: https://golang.org/doc/install
```
Build latest:
```bash
$ go get github.com/darklynx/request-baskets
```

Run:

```bash
$ export PATH=$PATH:$GOPATH/bin
```
```bash
chmod 777 ~/go/bin/./request-baskets
```
```bash
~/go/bin/./request-baskets 2>&1 | tee test.config &
```

Then execute test suite by running testng.xml file in your favourite IDE or with: 
```
mvn clean test 
```


