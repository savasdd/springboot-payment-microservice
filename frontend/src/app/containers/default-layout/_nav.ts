import { INavData } from '@coreui/angular';

export const navItems: INavData[] = [
  {
    name: 'Dashboard',
    url: '/dashboard',
    iconComponent: { name: 'cil-speedometer' }
  },
  {
    name: 'Siparişlerim',
    url: '/foods',
    iconComponent: { name: 'cil-list' },
    children: [
      {
        name: 'Food',
        url: 'foods/foods'
      },
      {
        name: 'Orders',
        url: 'foods/orders'
      },
      {
        name: 'Category',
        url: 'foods/categorys'
      },
      {
        name: 'Restaurant',
        url: 'foods/restaurants'
      },
      {
        name: 'Payment',
        url: 'foods/payments'
      },
    ]
  },
  {
    name: 'Stok',
    url: '/data',
    iconComponent: { name: 'cilBookmark' },
    children: [
      {
        name: 'Stok',
        url: 'foods/stocks'
      },
    ]
  },
  {
    name: 'Yetkilendirme',
    url: '/users',
    iconComponent: { name: 'cil-user' },
    children: [
      {
        name: 'Kullanıcı',
        url: 'users/user'
      },
      {
        name: 'Şehir',
        url: 'users/city'
      },
      {
        name: 'Rol',
        url: 'users/rol'
      },

    ]
  },
  {
    name: 'Çıkış',
    url: '/login',
    iconComponent: { name: 'cilLockLocked' },
    // children: [
    //   {
    //     name: 'Login',
    //     url: '/login'
    //   },
    //   // {
    //   //   name: 'Error 404',
    //   //   url: '/404'
    //   // },
    //   // {
    //   //   name: 'Error 500',
    //   //   url: '/500'
    //   // }
    // ]
  },
];
