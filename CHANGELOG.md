# CHANGELOG

## [UNRELEASED]
### Added
* Added option to load filter expression from a file (`-f` or `--from-file`). It uses Clojure's own parser, so anything the language supports should work. Only caveat is that the last form of the file must be a function that takes one argument: the input.
* Added option to load data readers from a file. The file format is the same as the one Clojure uses for [`*data-readers*`](https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/%2Adata-readers%2A).
* Added option to load EDN files by name instead of reading from stdin.
* Unit tests

### Changed
* Broken `cq.cli` into three namespaces: `cq.utils`, `cq.core` and `cq.cli` to make testing easier

## [0.3.0] - 2019-05-23
### Changed
* Command line arguments are parsed with tools.cli now.
* Exit status defaults to 1 when an exit message is returned on argument validation.

### Added
* Option to define the default reader fn from the CLI arguments.
* Option to colorize the output.

### Removed
* Native image is no longer used because GraalVM [does not support dynamic class loading](https://github.com/oracle/graal/blob/master/substratevm/LIMITATIONS.md#dynamic-class-loading--unloading) and Clojure's `eval` depends on it.
* toops.deps is no longer used, this is a leiningen project now.
* Removed puget, using zprint for pretty printing now.

## [0.2.0] - 2018-10-05
### Changed
* Replaced launch script in favor of native image built with GraalVM.

## [0.1.0] - 2018-09-05
### Added
* Basic EDN pretty printing.
* Custom function on parsed data from CLI args.
* Script to launch cq.clj.
