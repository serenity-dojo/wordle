import { SERVER_API_URL } from "../config";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';

axios.defaults.headers.common['Content-Type'] = 'application/json';
axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
const STORAGE_KEY = "token";

axios.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  console.log('error >>> ', error);
  if (error.response.status === 401 && error.response.data.status === 'fail' && error.response.data.message.name === 'TokenExpiredError') {
    window.location.href = '/auth';
  } else {
    return Promise.reject(error);
  }
});

axios.interceptors.request.use(function (config) {
  const accessToken = sessionStorage.getItem(STORAGE_KEY);
  if (accessToken != null) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }
  return config;
}, function (error) {
  return Promise.reject(error);
});

class AccountApi {
  me(request) {
    const { accessToken } = request

    return new Promise((resolve, reject) => {
      try {
        // Decode access token
        const decodedToken = jwtDecode(accessToken)
        console.log(' >>> decodeToken >>> ', decodedToken);

        const currrentTime = Date.now() / 1000;

        if (decodedToken.expires < currrentTime) {
          // Token has expired
          reject(new Error('Token expired'));
        } else {
          const { _id, name, email } = decodedToken;
          resolve({
            _id: _id,
            name: name,
            email: email
          });
        }
      } catch (err) {
        console.error('[Account Api]: ', err)
        reject(new Error('Internal server error'))
      }
    })
  }
}

export const accountApi = new AccountApi()