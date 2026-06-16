import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { logout } from '@/store/slices/authSlice';
import {
  LayoutDashboard,
  ClipboardList,
  FilePlus,
  FileText,
  LogOut,
  Clock,
  User,
  BarChart3,
  Shield,
  ExternalLink,
} from 'lucide-react';

const REPORTS_APP_URL = 'https://testmaq.sharepoint.com/myspace/Pages/MySpace.aspx#';
const ADMIN_APP_URL = 'http://localhost:5173/';

const Sidebar = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const user = useAppSelector((state) => state.auth.user);

  const handleLogout = () => {
    dispatch(logout());
    navigate('/login');
  };

  const navItems = [
    { path: '/dashboard', icon: LayoutDashboard, label: 'Dashboard' },
    { path: '/pending-approvals', icon: Clock, label: 'Pending Approvals' },
    { path: '/create-request', icon: FilePlus, label: 'Create Request' },
    { path: '/my-requests', icon: FileText, label: 'My Requests' },
  ];

  return (
    <aside className="fixed left-0 top-0 h-screen w-64 bg-sidebar flex flex-col z-50">
      {/* Logo */}
      <div className="p-6 border-b border-sidebar-border">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl btn-gradient flex items-center justify-center">
            <ClipboardList className="w-5 h-5 text-sidebar-primary-foreground" />
          </div>
          <div>
            <h1 className="text-lg font-bold text-sidebar-foreground">
              RequestFlow
            </h1>
            <p className="text-xs text-sidebar-muted">Management System</p>
          </div>
        </div>
      </div>

      {/* Navigation */}
      <nav className="flex-1 p-4 space-y-1">
        {navItems.map((item) => {
          const isActive = location.pathname === item.path;
          return (
            <Link
              key={item.path}
              to={item.path}
              className={`sidebar-link ${isActive ? 'sidebar-link-active' : ''}`}
            >
              <item.icon className="w-5 h-5" />
              <span>{item.label}</span>
            </Link>
          );
        })}

        {/* Divider */}
        <div className="my-3 border-t border-sidebar-border" />

        {/* External Apps */}
        <a
          href={REPORTS_APP_URL}
          target="_blank"
          rel="noopener noreferrer"
          className="sidebar-link"
        >
          <BarChart3 className="w-5 h-5" />
          <span className="flex-1">Reports</span>
          <ExternalLink className="w-4 h-4 opacity-60" />
        </a>

        <a
          onClick={(e)=> e.preventDefault()}
          href={ADMIN_APP_URL}
          target="_blank"
          rel="noopener noreferrer"
          className="sidebar-link"
          
        >
          <Shield className="w-5 h-5" />
          <span className="flex-1">Admin Panel</span>
          <ExternalLink className="w-4 h-4 opacity-60" />
        </a>
      </nav>

      {/* User Info */}
      <div className="p-4 border-t border-sidebar-border">
        <div className="flex items-center gap-3 mb-4 p-3 rounded-lg bg-sidebar-accent">
          <div className="w-10 h-10 rounded-full bg-primary/20 flex items-center justify-center">
            <User className="w-5 h-5 text-primary" />
          </div>
          <div className="flex-1 min-w-0">
            <p className="text-sm font-medium text-sidebar-foreground truncate">
              {user?.name}
            </p>
            <span className="text-xs px-2 py-0.5 rounded-full bg-primary/20 text-primary capitalize">
              {user?.role}
            </span>
          </div>
        </div>

        <button
          onClick={handleLogout}
          className="sidebar-link w-full justify-center hover:bg-destructive/10 hover:text-destructive"
        >
          <LogOut className="w-5 h-5" />
          <span>Logout</span>
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;
