// demo data only

import { User } from '@/store/slices/authSlice';
import { Request } from '@/store/slices/requestsSlice';
import { mockUsers } from '@/store/slices/authSlice';

export const demoApi = {
  loginAs: (role: 'employee' | 'poc' | 'manager'): User =>
    mockUsers.find((u) => u.role === role)!,

  getRequests: (): Request[] => {
    // return demo request list
    return [];
  },
};
