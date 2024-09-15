#! /bin/zsh

red='\e[1;31m'
green='\e[1;32m'
yellow='\e[1;33m'
blue='\e[1;34m'
purple='\e[1;35m'
cyan='\e[1;36m'
white='\e[1;37m'
orange='\e[38;5;208m'
reset='\e[0m'

#git utils
branchName=$(git rev-parse --abbrev-ref HEAD)
stagedFiles=$(git diff --cached --name-only --diff-filter=d)
stagedKotlinFiles=$(git diff --cached --name-only --diff-filter=d | grep -E '\.kt$|\.kts$')
min_char_commit_size=5
conventionnal_commit_pattern='^(chore|ci|docs|feat|fix|perf|refactor|style|test)\(((hooks)\-[0-9]+)\): [A-Za-z0-9\-_ ]{6,}'
branch_pattern='^(chore|ci|docs|feat|fix|perf|refactor|style|test)\/((hooks)\-[0-9]+)_[A-Za-z0-9\-_]{6,}$'

#function utils
function progress() {
    printf "${cyan}🔵 ${1}${reset}\n"
}

function printSuccess() {
    printf "${green}🟢 $1${reset}\n"
}
function printWarning() {
     printf "${orange}🟠 $1${reset}\n"
}

function exitWithMessage() {
    printf "${red}🔴 $1${reset}\n"
    exit 1
}

# $1 = title
# $2 = content
# Bring android studio on foreground
function showFailureNotification() {
    terminal-notifier -title "🔴 $1" -message "$2" -sound Submarine -group 12 -remove 12 -activate "com.google.android.studio" -contentImage android.png -ignoreDnD
}

# $1 = title
# $2 = content
# todo add execute with $3
function showSuccessNotification() {
    terminal-notifier -title "🟢 $1" -message "$2" -sound defaut -group 12 -remove 12  -contentImage android.png -ignoreDnD
}