import http from 'k6/http';
import { check, sleep } from 'k6';
import { uuidv4 } from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";

const BASE_URL = __ENV.BASE_URL

export const options = {
  vus: 5,
  duration: '30s',
};

function createUser(username, password) {
    const userRequest = {
        username: username,
        password: password,
        email: `test_${uuidv4()}@example.com`
      };
    
      return http.post(
        `${BASE_URL}/users/addNewUser`,
        JSON.stringify(userRequest),
        { headers: { 'Content-Type': 'application/json' , 'Connection': 'close'} }
      );
}

function generateToken(userName, password) {
     // Generate token
  const authRequest = {
    username: userName,
    password: password
  };

  return http.post(
    `${BASE_URL}/users/generateToken`,
    JSON.stringify(authRequest),
    { headers: { 'Content-Type': 'application/json', 'Connection': 'close' } }
  );
}

function createChannel(token, usersInChannel) {
      // Create channel
  const channelRequest = {
    name: `test_channel_${uuidv4()}`,
    description: 'Test channel description',
    members: usersInChannel  // You might want to add the user's ID here
  };
  
  return http.post(
    `${BASE_URL}/channels`,
    JSON.stringify(channelRequest),
    {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        'Connection': 'close'
      }
    }
  );
}


function createChat(token, usersInChannel) {
    const chatRequest = {
        name: `test_chat_${uuidv4()}`,
        description: 'Test chat description',
        members: usersInChannel  // You might want to add the user's ID here
      };
      
      return http.post(
        `${BASE_URL}/chats`,
        JSON.stringify(chatRequest),
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
            'Connection': 'close'
          }
        }
      );
}

function sendMessage(token, channelId) {
    const messageRequest = {
        conversationId: channelId,
        message: `Test message`
      };
  
      return http.post(
        `${BASE_URL}/messages`,
        JSON.stringify(messageRequest),
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
            'Connection': 'close'
          }
        }
      );
}

function searchMessages(token, channelId, startEpoch, endEpoch) {
    return http.get(
        `${BASE_URL}/conversations/search?conversationId=${channelId}&startEpoch=${startEpoch}&endEpoch=${endEpoch}`,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Connection': 'close'
          }
        }
      );
}

export default function () {
  // Create user
  const userName = `testuser_${uuidv4()}`
  const password = 'password_1234'

  const userResponse = createUser(userName, password)
  check(userResponse, {
    'user created': (r) => r.status === 200,
  });

  //Generate token
  const tokenResponse = generateToken(userName, password)

  check(tokenResponse, {
    'token generated': (r) => r.status === 200,
  });
  console.log(tokenResponse)
  const parsedTokenResponse = JSON.parse(tokenResponse.body)
  const token = parsedTokenResponse.token
  const userId = parsedTokenResponse.userId;

  // Create channel
  const channelResponse = createChannel(token, [userId])


  check(channelResponse, {
    'channel created': (r) => r.status === 200,
  });

  const channelId = JSON.parse(channelResponse.body).id;
  const channelMessageGenerationCount = 10
  // Send 10 messages to channel
  for (let i = 0; i < channelMessageGenerationCount; i++) {
    const messageResponse = sendMessage(token, channelId)
    check(messageResponse, {
      'message sent': (r) => r.status === 200,
    });

  }
  sleep(0.1)
  // Search messages by timestamp
  const startTime = 0; // Beginning of time
  const endTime = Date.now(); // Current time

  const searchResponse = searchMessages(token, channelId, startTime, endTime)
  check(searchResponse, {
    'search successful': (r) => r.status === 200,
    'found all messages': (r) => JSON.parse(r.body).length === channelMessageGenerationCount,
  });

  
  // Create chat
  const chatResponse = createChat(token, [userId])


  check(chatResponse, {
    'Chat created': (r) => r.status === 200,
  });

  const chatId = JSON.parse(chatResponse.body).id;

  const chatMessageGenerationCount = 10
  // Send 10 messages to channel
  for (let i = 0; i < chatMessageGenerationCount; i++) {
    const messageResponse = sendMessage(token, chatId)
    check(messageResponse, {
      'Chat message sent': (r) => r.status === 200,
    });

  }
  sleep(0.1)
  // Search messages by timestamp
  const chatStartTime = 0; // Beginning of time
  const chatEndTime = Date.now(); // Current time

  const chatSearchResponse = searchMessages(token, channelId, chatStartTime, chatEndTime)
  check(chatSearchResponse, {
    'Chat search successful': (r) => r.status === 200,
    'Chat found all messages': (r) => JSON.parse(r.body).length === chatMessageGenerationCount,
  });
}