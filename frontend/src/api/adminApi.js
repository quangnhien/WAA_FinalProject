import api from "../api";

export const getProperties = async () => {
    try {
        const respone = await api.get('/properties');
        return respone
    } catch (err) {
        throw err;
    }
};
export const getUsers = () => {};
export const getOffers = () => {};
export const activeOrDeactiveOwnerAccount = () => {};
export const approveProperty = () => {};
