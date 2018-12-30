# CHANGELOG

## [Unreleased]
### Changed
* Command line arguments are parsed with tools.cli now.
* Exit status defaults to 1 when an exit message is returned on argument validation.

### Added
* Option to define the default reader fn from the CLI arguments.
* Option to colorize the output.

### Removed
* Native image is no longer used because GraalVM [does not support dynamic class loading](https://github.com/oracle/graal/blob/master/substratevm/LIMITATIONS.md#dynamic-class-loading--unloading) and Clojure's `eval` depends on it.

## [0.2.0] - 2018-10-05
### Changed
* Replaced launch script in favor of native image built with GraalVM.

## [0.1.0] - 2018-09-05
### Added
* Basic EDN pretty printing.
* Custom function on parsed data from CLI args.
* Script to launch cq.clj.
