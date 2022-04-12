#!/bin/bash

printf -v date '%(%Y-%m-%d)T' -1
if [[ ! -f daily_standups/${date}_standup.md ]]; then
    echo "# ${date} Daily Standup -- Team Epimetheus" | cat - daily_standups/template.md > daily_standups/${date}_standup.md
fi
code daily_standups/${date}_standup.md