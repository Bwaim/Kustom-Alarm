#! /bin/zsh

echo "Refreshing Githooks"
git config --local core.hooksPath config/githooks/

chmod +x githooks/post-checkout
chmod +x githooks/post-merge
chmod +x githooks/pre-commit
chmod +x githooks/pre-push

#echo "Refreshing the Gitconfig" #bonus, nothing related to githooks, for team aliases
#git config --local include.path .gitconfig

echo "Installating toolings for the githooks"
HOMEBREW_AUTO_UPDATE_SECS=86400 brew bundle #brew  will update once per day

source ~/.zshrc #refresh your $PATH etc.
echo "🟢 End of configuration"