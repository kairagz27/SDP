#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mode="${1:-demo}"
case "$mode" in
  demo) target="kz.agzam.carwash.client.Main" ;;
  test) target="kz.agzam.carwash.facade.CarWashTests" ;;
  direct) target="kz.agzam.carwash.advanced.DirectSubsystemExample" ;;
  *) echo "Usage: bash run.sh [demo|test|direct]" >&2; exit 2 ;;
esac
mkdir -p build/classes
find src -name '*.java' -print > build/sources.txt
java com.sun.tools.javac.Main --release 17 -Xlint:all -Werror -d build/classes @build/sources.txt
java -cp build/classes "$target"
