#!/usr/bin/env sh
commit(){
    #echo "Please enter a commit message..."
    read -d# -p "Please enter a commit message...\n" msg
    git add --all
    git commit -m "$msg"
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
        echo "Would you like to commit them (y/n)?"
        read n
        case $n in
            "y") commit;;
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