/** Definiuje kolekcję ścieżek (kontekstów URL), które mogą prowadzić do widoków aplikacji
 */
export const Pathnames = {
    anonymous: {
    },
    user: {
        homePage: '/user',
        listVMachines: '/user/vmachine/list',
        userProfile: '/user/profile',
        myRents: '/user/rents',
        editProfile: '/user/edit',
    },
    admin: {
        homePage: '/admin',
        listUsers: '/admin/listUsers',
        userProfile: '/admin/profile',
        editProfile: '/admin/edit',
        createUser: '/admin/create',
    },

    moderator: {
        homePage: '/moderator',
        userProfile: '/moderator/profile',
        createVMachine: '/moderator/vmachine/create',
        listVMachines: '/moderator/vmachine/list',
        editProfile: '/moderator/edit',
    },

    default: {
        homePage: '/',
        login: '/login',
        createUser: '/register'
    }
}