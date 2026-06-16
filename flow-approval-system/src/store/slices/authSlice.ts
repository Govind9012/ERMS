import { createSlice, PayloadAction } from "@reduxjs/toolkit";

export type UserRole = "employee" | "poc" | "manager";

export interface User {
  id: string;
  employeeId: string;
  email?: string;
  name: string;
  role: UserRole;
  department: string;
  pocId?: string;
  managerId?: string;
}

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  isDemo: boolean; // 🔥 NEW
  loading: boolean;
}

/* =====================
   DEMO USERS (ONLY USED WHEN isDemo = true)
   ===================== */
export const mockUsers: User[] = [
  {
    id: "1",
    employeeId: "EMP001",
    email: "john.doe@company.com",
    name: "John Doe",
    role: "employee",
    department: "Engineering",
    pocId: "2",
    managerId: "3",
  },
  {
    id: "2",
    employeeId: "POC001",
    email: "jane.smith@company.com",
    name: "Jane Smith",
    role: "poc",
    department: "Engineering",
    managerId: "3",
  },
  {
    id: "3",
    employeeId: "MGR001",
    email: "mike.johnson@company.com",
    name: "Mike Johnson",
    role: "manager",
    department: "Engineering",
  },
];

const initialState: AuthState = {
  user: null,
  isAuthenticated: false,
  isDemo: false, // 🔥 NEW
  loading: false,
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    login: (state, action: PayloadAction<{ user: User; isDemo?: boolean }>) => {
      state.user = {
        ...action.payload.user,
        role: action.payload.user.role.toLowerCase() as UserRole,
      };

      state.isAuthenticated = true;
      state.isDemo = action.payload.isDemo ?? false;
      state.loading = false;

      localStorage.setItem(
        "auth",
        JSON.stringify({
          user: state.user,
          isDemo: state.isDemo,
        }),
      );
    },

    logout: (state) => {
      state.user = null;
      state.isAuthenticated = false;
      state.isDemo = false;
      state.loading = false;
      localStorage.removeItem("auth");
    },

    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload;
    },
  },
});

export const { login, logout, setLoading } = authSlice.actions;
export default authSlice.reducer;
