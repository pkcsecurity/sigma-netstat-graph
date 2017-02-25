# imb-deps

![Yay for Sigma Graphs](https://github.com/pkcsecurity/sigma-netstat-graph/blob/cafee764e94a7bc076c10792c81d5c7d6d607f58/generated-graph.png)

## Installation

Ask Ken for data.tar.gz, put in the top level and unzip.

## Usage

1. run `lein repl`, run `(-main)`. This will take the netstat data in data/ and convert it to nodes/edges in sigma.json, inside `src/graph-sigma/build`
2. go into src/graph-sigma/build, run an http server (ex. `http-server`)
3. open your browser, go to localhost, and check it out!

## Adding more netstats or netstat csv's
1. CSV's were generated with `netstat -ano` on Windows. Filenames MUST have the form `<fully-qualified hostname>.csv`
2. Netstats were run as `netstat -a` on Windows. Filenames MUST have the form `<fully-qualified hostname>`

## Limitations
1. Only tested on Windows netstats :(
