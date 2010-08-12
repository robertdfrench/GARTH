## Install directory hardcoded for Mac OS X
WEB_INSTALL = /Library/WebServer/Documents/garth/

#####################
## install: default target
## Creates appropriate directories if they don't exist
## Copies what files have changed
#####################
install: index.html style.css includes/DbConnector.php garth_main.js jquery-1.4.2.min.js experiment_list.php experiment_data.php
	if [ -d $(WEB_INSTALL) ]; then ``; else mkdir $(WEB_INSTALL); fi
	if [ -d $(WEB_INSTALL)/includes/ ]; then ``; else mkdir $(WEB_INSTALL)/includes/; fi 
	cp index.html $(WEB_INSTALL)
	cp style.css $(WEB_INSTALL)
	cp garth_main.js $(WEB_INSTALL)
	cp jquery-1.4.2.min.js $(WEB_INSTALL)
	cp experiment_list.php $(WEB_INSTALL)
	cp experiment_data.php $(WEB_INSTALL)
	cp includes/DbConnector.php $(WEB_INSTALL)/includes/
