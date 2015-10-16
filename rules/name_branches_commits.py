import re
import subprocess

git_message_to_branch = {}

#BRANCH_REGEX = re.compile('origin/(\d[^,]*)[\),]')

BRANCH_NAME_REGEX = re.compile('\d\.\d\d') #find the commit names
COMMIT_REGEX = re.compile('commit ([0-9a-f]{5,40})')

def sanitize(message):
	return message.strip().replace(' - ', '-').replace(' ', '_').lower()
	#return message.strip().replace('(', '\(').replace(')', '\)')

# git.log is the output from git log before rebasing, with all the branch labels
# we look for all the branch labels in it and grab the commit messages associated
# with each of them, so that we can look up branch by message name later
with open("git.log") as git_log:
	commitline = git_log.readline()
	while commitline:
		matchCommit = re.search(COMMIT_REGEX, commitline)
		if matchCommit != None:
			commit = matchCommit.group(1)
			print commit
			for i in range(3): git_log.readline()
			branchline = git_log.readline()
			branchName = sanitize(branchline)
			#branchName = matchBranchName.group(0)
			print branchline
			git_message_to_branch[commit] = branchName
		commitline = git_log.readline()

print git_message_to_branch

# Now take the current state of the log, which should have the same commit messages
# for the most part, but the commit SHAs will have changed
# So, we find the new SHAs associated with each message, and re-attach the branches
# to those commits!
# p = subprocess.Popen(['git','log'], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
# output, err = p.communicate()

for commit in git_message_to_branch:
  #commit_regex = re.compile('commit ([^ ]*)([^\n]*\n){4}    ' + message)
  #match = re.search(commit_regex, output)
  #if match != None and message in git_message_to_branch:
  #commit = match.group(1)
  #subprocess.call(['git','branch', '-D', git_message_to_branch[commit]])
  subprocess.call(['git','branch', git_message_to_branch[commit],commit])
  # else: 
  #     print "Couldn't find " + message
