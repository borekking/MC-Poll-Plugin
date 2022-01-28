# MC-Poll-Plugin
Minecraft Plugin for doing ingame Polls in an 1.8.8 singel minecraft server (spigot recommended)

# Features
### 1) Creating a Poll via GUI (/poll create)
Configurable: 
- Name
- Question
- Upto six answer options
- Polls length (time)

### 2) Voting in a Poll via GUI (/vote <poll-name>)
You can vote for one answer by clicking it. As well you can see the poll's quetions and it's time left.

### 3) Viewing a polls results (/poll result <poll-name>)
After a poll ended you can see its results in a GUI.

### 4) Poll seralizing
Polls are seralized into a yml-file such that the server can be restarted and the polls will be there. (Active and Ended Polls)

# Permissions
- /poll create: poll.create
- /poll result <poll-name>: poll.result
- /vote <poll-name>: poll.vote
