wget -c https://golang.org/dl/go1.15.5.linux-amd64.tar.gz

sudo tar -C . -xvzf go1.15.5.linux-amd64.tar.gz

export  PATH=$PATH:/usr/local/go/bin

go get github.com/darklynx/request-baskets

chmod 777 ~/go/bin/./request-baskets

~/go/bin/./request-baskets 2>&1 | tee test.config &