import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { createAsyncThunk } from "@reduxjs/toolkit";

export type RequestType = "wfh" | "leave" | "shift" | "resource";
export type RequestStage =
  | "pending_poc"
  | "pending_manager"
  | "approved"
  | "rejected_poc"
  | "rejected_manager";

export type RequestStatus = "pending" | "approved" | "rejected" | "cancelled";

const REQUEST_TYPE_TO_BACKEND: Record<RequestType, string> = {
  wfh: "WORK_FROM_HOME",
  leave: "LEAVE",
  shift: "SHIFT_CHANGE",
  resource: "RESOURCE",
};

const REQUEST_STAGE_FROM_BACKEND: Record<string, RequestStage> = {
  POC_PENDING: "pending_poc",
  MANAGER_PENDING: "pending_manager",
  // COMPLETED: "approved",
  REJECTED_POC: "rejected_poc",
  REJECTED_MANAGER: "rejected_manager",
};

const REQUEST_STATUS_FROM_BACKEND: Record<string, RequestStatus> = {
  PENDING: "pending",
  APPROVED: "approved",
  REJECTED: "rejected",
  CANCELLED: "cancelled",
};

const REQUEST_TYPE_FROM_BACKEND: Record<string, RequestType> = {
  WORK_FROM_HOME: "wfh",
  LEAVE: "leave",
  SHIFT_CHANGE: "shift",
  RESOURCE: "resource",
};

export interface Request {
  id: string;
  type: RequestType;
  title: string;
  description: string;

  employeeId?: string;
  employeeName?: string;
  pocId?: string;
  managerId?: string;

  stage: RequestStage;
  status: RequestStatus;

  createdAt: string;
  updatedAt?: string;

  startDate?: string;
  endDate?: string;

  pocRemark?: string;
  managerRemark?: string;

  isImportant?: boolean; // 👈 make OPTIONAL
}

interface RequestsState {
  myRequests: Request[];
  pendingApprovals: Request[];
  loading: boolean;
}

/* =====================
   DEMO REQUESTS
   ===================== */
const initialRequests: Request[] = [
  {
    id: "1",
    type: "wfh",
    title: "Work From Home - Personal Reasons",
    description: "Requesting WFH for next week due to home renovation work.",
    employeeId: "1",
    employeeName: "John Doe",
    pocId: "2",
    managerId: "3",
    status: "pending", // ❌ not "pending_poc"
    stage: "pending_poc",

    isImportant: true,
    createdAt: "2024-01-10T09:00:00Z",
    updatedAt: "2024-01-10T09:00:00Z",
    startDate: "2024-01-15",
    endDate: "2024-01-19",
  },
];

const initialState: RequestsState = {
  myRequests: [],
  pendingApprovals: [],
  loading: false,
};

export const fetchMyRequests = createAsyncThunk(
  "requests/fetchMyRequests",
  async (employeeId: string, { rejectWithValue }) => {
    try {
      const res = await fetch("http://localhost:8080/api/requests/my", {
        headers: {
          "Content-Type": "application/json",
          "X-EMPLOYEE-ID": employeeId,
        },
      });

      if (!res.ok) {
        return rejectWithValue("Failed to fetch requests");
      }

      return await res.json();
    } catch {
      return rejectWithValue("Network error");
    }
  },
);

const requestsSlice = createSlice({
  name: "requests",
  initialState,
  reducers: {
    addRequest: (state, action: PayloadAction<Request>) => {
      state.myRequests.unshift(action.payload);
    },

    clearAllRequests: (state) => {
      state.myRequests = [];
      state.pendingApprovals = [];
    },

    loadDemoRequests: (state) => {
      state.myRequests = initialRequests;
    },

    updateRequestStatus: (
      state,
      action: PayloadAction<{
        id: string;
        status: RequestStatus;
        remark?: string;
        isManager?: boolean;
      }>,
    ) => {
      const request = state.myRequests.find((r) => r.id === action.payload.id);

      if (request) {
        request.status = action.payload.status;
        request.updatedAt = new Date().toISOString();

        if (action.payload.remark) {
          if (action.payload.isManager) {
            request.managerRemark = action.payload.remark;
          } else {
            request.pocRemark = action.payload.remark;
          }
        }
      }
    },

    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchMyRequests.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchMyRequests.fulfilled, (state, action) => {
        state.myRequests = action.payload.map((r: any) => ({
          id: r.requestId,
          title: r.title,
          description: r.description,
          createdAt: r.createdAt,
          startDate: r.startDate,
          endDate: r.endDate,
          type: REQUEST_TYPE_FROM_BACKEND[r.requestType],
          stage: REQUEST_STAGE_FROM_BACKEND[r.stage],
          status: REQUEST_STATUS_FROM_BACKEND[r.status],
          isImportant: r.important ?? false,
        }));
      })

      .addCase(fetchPendingApprovals.fulfilled, (state, action) => {
        state.pendingApprovals = action.payload.map((r: any) => ({
          id: r.requestId,
          title: r.title,
          description: r.description,
          createdAt: r.createdAt,
          startDate: r.startDate,
          endDate: r.endDate,
          type: REQUEST_TYPE_FROM_BACKEND[r.requestType],
          stage: REQUEST_STAGE_FROM_BACKEND[r.stage],
          status: REQUEST_STATUS_FROM_BACKEND[r.status],
          isImportant: r.important ?? false,
        }));
      })

      .addCase(fetchMyRequests.rejected, (state) => {
        state.loading = false;
      })
      .addCase(processApprovalAction.fulfilled, (state, action) => {
        const { requestId } = action.payload;

        // remove from pending approvals
        state.pendingApprovals = state.pendingApprovals.filter(
          (r) => r.id !== requestId,
        );

        // Optional: mark locally updated (actual stage/status will come from refetch)
        const req = state.myRequests.find((r) => r.id === requestId);
        if (req) {
          req.updatedAt = new Date().toISOString();
        }
      })

      .addCase(createRequest.fulfilled, (state, action) => {
        const r = action.payload;

        state.myRequests.unshift({
          ...r,
          id: r.requestId,
          type: REQUEST_TYPE_FROM_BACKEND[r.requestType],
          status: REQUEST_STATUS_FROM_BACKEND[r.status],
          stage: REQUEST_STAGE_FROM_BACKEND[r.stage],
        });
      });
  },
});

export const createRequest = createAsyncThunk(
  "requests/createRequest",
  async (
    data: {
      type: RequestType;
      title: string;
      description: string;
      isImportant: boolean;
      startDate?: string | null;
      endDate?: string | null;
    },
    { getState, rejectWithValue },
  ) => {
    try {
      const state: any = getState();
      const user = state.auth.user;

      if (!user?.employeeId) {
        return rejectWithValue("User not authenticated");
      }

      const res = await fetch("http://localhost:8080/api/requests", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-EMPLOYEE-ID": user.id, // 🔥 THIS WAS MISSING
        },
        body: JSON.stringify({
          requestType: REQUEST_TYPE_TO_BACKEND[data.type as RequestType],
          // 🔥 REQUIRED
          title: data.title,
          description: data.description,
          important: data.isImportant, // 🔥 REQUIRED
          startDate: data.startDate,
          endDate: data.endDate,
        }),
      });

      if (!res.ok) {
        const err = await res.json();
        return rejectWithValue(err.message || "Failed to create request");
      }

      return await res.json();
    } catch {
      return rejectWithValue("Network error");
    }
  },
);

export const fetchPendingApprovals = createAsyncThunk(
  "requests/fetchPendingApprovals",
  async (
    { role, employeeId }: { role: "poc" | "manager"; employeeId: string },
    { rejectWithValue },
  ) => {
    try {
      const url =
        role === "poc"
          ? "http://localhost:8080/api/approvals/poc"
          : "http://localhost:8080/api/approvals/manager";

      const res = await fetch(url, {
        headers: {
          "Content-Type": "application/json",
          "X-EMPLOYEE-ID": employeeId,
        },
      });

      if (!res.ok) {
        return rejectWithValue("Failed to fetch pending approvals");
      }

      return await res.json();
    } catch {
      return rejectWithValue("Network error");
    }
  },
);

export const processRequestAction = createAsyncThunk(
  "requests/processRequestAction",
  async (
    {
      requestId,
      action,
      remark,
    }: {
      requestId: string;
      action: "approve" | "reject";
      remark?: string;
    },
    { rejectWithValue },
  ) => {
    try {
      const res = await fetch(
        `http://localhost:8080/api/requests/${requestId}/${action}`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ remark }),
        },
      );

      if (!res.ok) {
        return rejectWithValue("Failed to update request");
      }

      return await res.json();
    } catch (err) {
      console.log(err.message + ` Our error`);
      return rejectWithValue("Network error");
    }
  },
);

export const processApprovalAction = createAsyncThunk(
  "requests/processApprovalAction",
  async (
    {
      role,
      requestId,
      action,
      remark,
    }: {
      role: "poc" | "manager";
      requestId: string;
      action: "approve" | "reject";
      remark?: string;
    },
    { getState, rejectWithValue },
  ) => {
    try {
      const state: any = getState();
      const employeeId = state.auth.user?.id;

      const res = await fetch(
        `http://localhost:8080/api/approvals/${role}/${requestId}/${action}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "X-EMPLOYEE-ID": employeeId,
          },
          body: JSON.stringify({ remark }),
        },
      );

      if (!res.ok) {
        return rejectWithValue("Failed to process request");
      }

      // ✅ backend returns NO body
      return { requestId };
    } catch {
      return rejectWithValue("Network error");
    }
  },
);

export const {
  addRequest,
  updateRequestStatus,
  setLoading,
  clearAllRequests,
  loadDemoRequests,
} = requestsSlice.actions;

export default requestsSlice.reducer;
