#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mode="${1:-demo}"
case "$mode" in
  demo) target="kz.agzam.study.client.Main" ;;
  test) target="kz.agzam.study.StudyPlannerTests" ;;
  *) echo "Usage: bash run.sh [demo|test]" >&2; exit 2 ;;
esac
mkdir -p build/classes
find src -name '*.java' -print > build/sources.txt
javac --release 17 -encoding UTF-8 -Xlint:all -Werror -d build/classes @build/sources.txt
java -cp build/classes "$target"
