// owner 
import OwnerDashboard from './components/Owner/OwnerDashboard.jsx';
import PropertyManagement from './components/Owner/PropertyManagement.tsx';
import Offers from './components/Owner/Offers.tsx';
import Messages from './components/Owner/Messages.tsx';
import { Navigate, Route, Routes } from 'react-router'
import AuthLayout from './containers/AuthLayout'
import Login from './components/Auth/Login'
import Logout from './components/Auth/Logout'
import MainLayout from './containers/MainLayout'
import Homepage from './components/Homepage'
import AdminLayout from './containers/AdminLayout'
import OwnerLayout from './containers/OwnerLayout'
import Signup from './components/Auth/Signup'
import CustomerLayout from './containers/CustomerLayout.jsx';
import CustomerDashboard from './components/Customer/CustomerDashboard.jsx';
import CurrentOffers from './components/Customer/CurrentOffers.jsx';
import OfferHistory from './components/Customer/OfferHistory.jsx';
import SavedProperties from './components/Customer/SavedProperties.jsx';
import NotFound from './components/NotFound.jsx';
import ProtectedRoute from './containers/ProtectedRoute.jsx';
import { UserRole } from './constants/role.js';

// admin
import CustomerManagement from './components/Admin/CustomerManagement.jsx';
import PropertiesManagement from './components/Admin/PropertiesManagement.jsx';
import OwnerManagement from './components/Admin/OwnerManagement.jsx';
import OfferManagement from './components/Admin/OfferManagement.jsx';
import Account from './components/Account.jsx';
import ResetPassword from './components/Auth/ResetPassword.jsx';

function App() {
  return (
    <Routes>
      <Route path='*' element={<NotFound />}/>

      <Route path="/logout" element={<Logout />}/>

      <Route path="/auth" element={<AuthLayout />}>
        <Route path="login" element={<Login />}/>
        <Route path="signup" element={<Signup />}/>
      </Route>

      <Route path="/" element={<MainLayout />}>
        <Route index element={<Homepage />}/>
      </Route>

      <Route path='/profile' element={<ProtectedRoute allowedRoles={[UserRole.ADMIN, UserRole.OWNER, UserRole.CUSTOMER]} />}>
        <Route element={<MainLayout />}>
          <Route path='account' element={<Account />}/>
          <Route path='reset-password' element={<ResetPassword />}/>
        </Route>
      </Route>

      <Route path="/admin" element={<ProtectedRoute allowedRoles={[UserRole.ADMIN]}/>}>
          <Route element={<AdminLayout />}>
            <Route index element={<Navigate to="/admin/properties" replace />} />
            <Route path="properties" element={<PropertiesManagement />} />
            <Route path="owners" element={<OwnerManagement />} />
            <Route path="customers" element={<CustomerManagement />} />
            <Route path="offers" element={<OfferManagement />} />
          </Route>
      </Route>

      <Route path="/owner" element={<ProtectedRoute allowedRoles={[UserRole.OWNER]}/>}>
        <Route element={<OwnerLayout />}>
          <Route index element={<OwnerDashboard />} />
          
          <Route path="property-management" element={<PropertyManagement/>} />
          <Route path="offers" element={<Offers/>} />
          <Route path="messages" element={<Messages/>} />
        </Route>
      </Route>

      <Route path="/customer" element={<ProtectedRoute allowedRoles={[UserRole.CUSTOMER]}/>}>
        <Route element={<CustomerLayout />}>
          <Route index element={<CustomerDashboard />} />
          
          <Route path="current-offers" element={<CurrentOffers/>} />
          <Route path="offer-history" element={<OfferHistory/>} />
          <Route path="saved-properties" element={<SavedProperties/>} />
        </Route>
      </Route>
      
    </Routes>
  )
}

export default App
