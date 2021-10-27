#!/usr/bin/env sh

REPO_NAME="2021-2022-Freight-Frenzy-7198"
GITHUB_DOMAIN="@github.com/anacortesftcrobotics/$REPO_NAME.git"
git_commit(){
    #echo "Please enter a commit message..."
    read -d# -p "Please enter a commit message and end with #: " msg
    git add --all
    git commit -m "$msg"
}

git_push() {
    read -p "Please enter github token: " token
    git push "https://$token$GITHUB_DOMAIN"
}

check_commit(){
    echo ========== CHECKING FOR CHANGES ========
    changes=$(git diff)
    if [ -n "$changes" ]; then
        echo ""
        echo "*** CHANGES FOUND ***"
        echo "$changes"
        echo ""
        echo "You have uncomitted changes."
        read -n 1 -p "Would you like to commit and push them (y/n)?" commit
        echo ""
        case $commit in
            "y") git_commit;;
            "n") echo "Changes will not be included...";;
            *) echo "invalid option";;
        esac
    else
        echo "... No changes found"
    fi
}

deploy(){
    cd ~/Desktop/2021-2022-Freight-Frenzy-7198
    check_commit
}

"$@"