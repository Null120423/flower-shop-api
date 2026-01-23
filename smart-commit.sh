#!/bin/bash

set -e

BASE_BRANCH="develop"
DATE=$(date +"%Y%m%d-%H%M%S")

echo "🔍 Checking git status..."

if ! git rev-parse --git-dir > /dev/null 2>&1; then
  echo "❌ Not a git repository"
  exit 1
fi

CURRENT_BRANCH=$(git branch --show-current)

echo "🌿 Current branch: $CURRENT_BRANCH"

# -----------------------------
# checkout new branch if needed
# -----------------------------
if [ "$CURRENT_BRANCH" = "$BASE_BRANCH" ]; then
  NEW_BRANCH="auto-${DATE}"
  echo "🚀 On develop → creating new branch: $NEW_BRANCH"
  git checkout -b "$NEW_BRANCH"
else
  echo "✅ Not on develop → keep branch: $CURRENT_BRANCH"
fi

# -----------------------------
# git add
# -----------------------------
echo "📦 Staging files..."
git add .

# -----------------------------
# check staged
# -----------------------------
if git diff --cached --quiet; then
  echo "⚠️ Nothing to commit"
  exit 0
fi

# -----------------------------
# generate commit message
# -----------------------------
FILES=$(git diff --cached --name-only)
COUNT=$(echo "$FILES" | wc -l | tr -d ' ')

TYPE="chore"

if echo "$FILES" | grep -qi "service"; then TYPE="feat"; fi
if echo "$FILES" | grep -qi "controller"; then TYPE="feat"; fi
if echo "$FILES" | grep -qi "fix"; then TYPE="fix"; fi
if echo "$FILES" | grep -qi "test"; then TYPE="test"; fi
if echo "$FILES" | grep -qi "doc"; then TYPE="docs"; fi

SCOPE=$(echo "$FILES" | head -n 1 | cut -d'/' -f1)

MESSAGE="$TYPE($SCOPE): update $COUNT file(s)"

echo "📝 Commit message:"
echo "👉 $MESSAGE"

# -----------------------------
# commit
# -----------------------------
git commit -m "$MESSAGE"

echo "✅ Commit done!"