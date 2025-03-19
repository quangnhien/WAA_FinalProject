import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./auth/authSlice";
import adminReducer from "./admin/adminSlice";

const store = configureStore({
  reducer: {
    auth: authReducer,
    admin: adminReducer
  },
});

export default store;
