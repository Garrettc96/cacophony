import http from 'k6/http';
import { check, sleep } from 'k6';
import { uuidv4 } from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

const BASE_URL = 'http://localhost:8080/cacophony';

export const options = {
  vus: 1,
  duration: '30s',
};

export default function () {
  // Create user
  const userRequest = {
    username: `testuser_${uuidv4()}`,
    password: 'password123',
    email: `test_${uuidv4()}@example.com`
  };

  const userResponse = http.post(
    `${BASE_URL}/users/addNewUser`,
    JSON.stringify(userRequest),
    { headers: { 'Content-Type': 'application/json' } }
  );

  check(userResponse, {
    'user created': (r) => r.status === 200,
  });

  // Generate token
  const authRequest = {
    username: userRequest.username,
    password: userRequest.password
  };

  const tokenResponse = http.post(
    `${BASE_URL}/users/generateToken`,
    JSON.stringify(authRequest),
    { headers: { 'Content-Type': 'application/json' } }
  );

  check(tokenResponse, {
    'token generated': (r) => r.status === 200,
  });

  const token = tokenResponse.body;

  // Create channel
  const channelRequest = {
    name: `test_channel_${uuidv4()}`,
    description: 'Test channel description',
    members: []  // You might want to add the user's ID here
  };

  const channelResponse = http.post(
    `${BASE_URL}/channels`,
    JSON.stringify(channelRequest),
    {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    }
  );

  check(channelResponse, {
    'channel created': (r) => r.status === 200,
  });

  console.log(channelResponse.body)
  const channelId = JSON.parse(channelResponse.body).id;

  // Send 10 messages
  for (let i = 0; i < 10; i++) {
    const messageRequest = {
      conversationId: channelId,
      message: `Test message ${i}`
    };

    const messageResponse = http.post(
      `${BASE_URL}/messages`,
      JSON.stringify(messageRequest),
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      }
    );

    check(messageResponse, {
      'message sent': (r) => r.status === 300,
    });

    sleep(0.1); // Small delay between messages
  }

  // Search messages by timestamp
  const startTime = 0; // Beginning of time
  const endTime = Date.now(); // Current time

  const searchResponse = http.get(
    `${BASE_URL}/conversations/search?conversationId=${channelId}&startEpoch=${startTime}&endEpoch=${endTime}`,
    {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    }
  );

  check(searchResponse, {
    'search successful': (r) => r.status === 200,
    'found all messages': (r) => JSON.parse(r.body).length === 10,
  });
}