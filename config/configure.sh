#! /bin/zsh

#
# Copyright (c) 2024 Dev Bwaim team
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

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