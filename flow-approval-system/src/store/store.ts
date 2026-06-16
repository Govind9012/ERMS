import { configureStore } from '@reduxjs/toolkit';
import authReducer from './slices/authSlice';
import requestsReducer from './slices/requestsSlice';


const persistedAuth = localStorage.getItem("auth");

const preloadedState = persistedAuth
  ? {
      auth: {
        user: JSON.parse(persistedAuth).user,
        isAuthenticated: true,
        isDemo: JSON.parse(persistedAuth).isDemo,
        loading: false,
      },
    }
  : undefined;


export const store = configureStore({
  reducer: {
    auth: authReducer,
    requests: requestsReducer,
  },
  preloadedState,
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
