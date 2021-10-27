#!/usr/bin/env sh

git_commit(){
    #read -d# -p "Please enter a multiline commit message and end with #: " msg
    echo "Please enter Notepad to create commit message"
    tmp="commit_msg.txt" # create_tempfile
    touch $tmp
    notepad $tmp
    msg=$(cat < "$tmp")
    rm -f -- "$tmp"
    
    git add --all
    git commit -m "$msg"
}

git_push() {
    read -p "Please enter github token: " token
    git push "https://$token$GITHUB_DOMAIN"
}

check_push(){
    read -n 1 -p "Would you like to push (y/n)?" push
    echo ""
    case $push in
        "y") git_push;;
        "n") echo "Changes will not be pushed...";;
        *) echo "invalid option";;
    esac
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
        read -n 1 -p "Would you like to commit (y/n)?" commit
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


run(){
    local run_mode="$1"
    if [[ "$run_mode" =~ ^("commit"|"push"|"commit+push")$ ]]; then
        FILE=".git"
        if [ ! -f "$FILE" ]; then
            SOURCE="${BASH_SOURCE[0]}"
            GOTO=`dirname $SOURCE | sed 's/^.\///' ` # get path without ./
            cd $GOTO
        fi
        CURRENT=`pwd`
        REPO_NAME=`basename "$CURRENT"`
        GITHUB_DOMAIN="@github.com/anacortesftcrobotics/$REPO_NAME$FILE"
        case $run_mode in
            "commit") check_commit;;
            "push") check_push;;
            "commit+push") check_commit;check_push;;
        esac
    else
        echo "Please call with one of the following parameters: commit, push, commit+push"
    fi
}

"$@"