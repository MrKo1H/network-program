session="subscriberd"
tmux new -d -s $session './Makefile'
window=0
tmux rename-window -t $session:$window 'subscriber'
tmux split-window -v -t $session:$window 'java Menu'
tmux attach-session -t $session:$window
