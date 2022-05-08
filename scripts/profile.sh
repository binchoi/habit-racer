#!/usr/bin/env bash

# Find unoccupied profile: if real1 is being used real2 is free and vice versa
function find_idle_profile()
{
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" https://habitracer.com/profile) # changed from localhost

  if [ ${RESPONSE_CODE} -ge 400 ] # if greater than 400 (i.e. 40x/50x errors)
  then
    CURRENT_PROFILE=real2
  else
    CURRENT_PROFILE=$(curl -s https://habitracer.com/profile) # changed from localhost: need to add license to localhost host name
  fi

  if [ ${CURRENT_PROFILE} == real1 ]
  then
    IDLE_PROFILE=real2
  else
    IDLE_PROFILE=real1
  fi

  echo "${IDLE_PROFILE}"
}

# Find unoccupied port
function find_idle_port()
{
  IDLE_PROFILE=$(find_idle_profile)

  if [ ${IDLE_PROFILE} == real1 ]
  then
    echo "8081"
  else
    echo "8082"
  fi
}