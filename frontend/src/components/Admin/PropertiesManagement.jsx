import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchPropertiesSuccess } from '../../store/admin/adminSlice';

const listProperties = [
  {
    id: 1,
    property: "Example",
    owner: {
      id: 1,
      name: "SEN"
    },
    status: "Pending"
  },
  {
    id: 2,
    property: "Example",
    owner: {
      id: 1,
      name: "Jim"
    },
    status: "Pending"
  },
  {
    id: 3,
    property: "Example",
    owner: {
      id: 1,
      name: "John"
    },
    status: "Pending"
  }
]

const PropertiesManagement = () => {
    const [tab, setTab] = useState(0);
    const dispatch = useDispatch();
    const {properties} = useSelector((state) => state.admin);
    const [searchText, setSearchText] = useState("");
    const renderBadgeClass = (status) => {
        return status === 'Approval' ? "badge-active" : "badge-deactive";
    };

  useEffect(() => {
    // fetch data
    dispatch(fetchPropertiesSuccess({data: listProperties}));
  }, [dispatch, tab]);

  const onHandleActiveAndDeactive = (id) => {
      const updatedOwners = properties.map((item) => 
          item.id === id ? { ...item, status: item.status === "Pending" ? "Approval" : "Pending" } : item
      );
  
      dispatch(fetchPropertiesSuccess({ data: updatedOwners }));
  };
  
    const handleSearchProperty = () => {
      // fetch API
      setSearchText("");
    }

  return (
    <div className="flex flex-col gap-4 w-full">
      <h2 className="text-2xl font-medium text-left">Propeties</h2>
      
      {/* tabs */}
      <div className='flex flex-row gap-0 items-center justify-start border-b border-gray-200 tabs'>
        <div className={`${tab === 0 ? 'active' : ''} tab-item`} onClick={() => setTab(0)}>All</div>
        <div className={`${tab === 1 ? 'active' : ''} tab-item`} onClick={() => setTab(1)}>Active</div>
      </div>

      {/* Search item */}
      <div className='flex flex-row justify-end w-full items-stretch'>
        <input value={searchText} onChange={(e) => setSearchText(e.target.value)} type="text" className='border border-gray-200 p-2 rounded-l-md outline-0' />
        <button onClick={handleSearchProperty} className='search-button'>Search</button>
      </div>

      {/* table */}
      <div className='flex flex-row gap-2 w-full'>
        <table className="border-collapse table-fixed w-full">
          <thead>
            <tr>
              {/* <th><input type="checkbox" name="check_all" /></th> */}
              <th>Id</th>
              <th>Property</th>
              <th>Owner</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>

            <tbody>
                {properties && properties.length > 0 ? (
                    <>
                    {
                        properties.map((item, key) => (
                            <tr key={key}>
                                {/* <td><input type='checkbox' name={item.id}/></td> */}
                                <td>{item.id}</td>
                                <td>{item.property}</td>
                                <td>{item?.owner?.name}</td>
                                <td><span className={`badge ${renderBadgeClass(item.status)}`}>{item.status}</span></td>
                                <td>
                                    <button 
                                        onClick={() => onHandleActiveAndDeactive(item.id)} 
                                        className={`${item.status !== 'Pending' ? 'hidden' : ''} text-sky-600 bg-slate-100 font-bold text-sm px-2 cursor-pointer`}
                                    >
                                        Approval
                                    </button>
                                </td>
                            </tr>
                        ))
                    }
                    </>
                ) : (
                <tr>
                    <td colSpan="6" className="text-center py-4">No property found.</td>
                </tr>
            )}
            </tbody>
        </table>
      </div>
    </div>
  );
}

export default PropertiesManagement;