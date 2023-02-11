export const getWidthDetails = (count) => {
    let width;
    switch (count) {
        case 7:
          width = 75;
          break;
        case 6:
          width = 50;
          break;
        case 5:
          width = 50;
          break;
        case 4:
          width = 40;
          break;
        case 3:
          width = 40;
          break;
      }
    return width;
};

export const Default_Icons_Info = [
  {
    key: 'view_order_on_dashboard',
    name: 'Orders',
    localisationKey: 'orders',
    icon: 'assets/Images/lobby/orders.svg',
    gridIcon: 'assets/Images/grid/orders.svg',
    navigateUrl: `/connect_ui/#/orders`
  },
  {
    key: 'view_workflow_on_dashboard',
    name: 'Workflow',
    localisationKey: 'workflow',
    icon: 'assets/Images/lobby/workflow.svg',
    gridIcon: 'assets/Images/grid/workflow.svg',
    navigateUrl: `/connect_ui/#/workflow`
  },
  {
    key: 'view_nipt_on_dashboard',
    name: 'NIPT Analysis',
    localisationKey: 'niptAnalysis',
    icon: 'assets/Images/lobby/NIPTAnalysis.svg',
    gridIcon: 'assets/Images/grid/NIPTAnalysis.svg',
    navigateUrl: null
  },
  {
    key: '',
    name: 'Place Holder',
    localisationKey: 'dummy',
    icon: 'assets/Images/lobby/orders.svg',
    gridIcon: 'assets/Images/grid/orders.svg',
    navigateUrl: null
  },
  {
    key: 'view_connections_on_dashboard',
    name: 'Connections',
    localisationKey: 'connections',
    icon: 'assets/Images/lobby/connections.svg',
    gridIcon: 'assets/Images/grid/connections.svg',
    navigateUrl: `/admin_ui/#/device-list`
  },
  {
    key: 'view_settings_on_dashboard',
    name: 'Administration',
    localisationKey: 'administration',
    icon: 'assets/Images/lobby/administration.svg',
    gridIcon: 'assets/Images/grid/administration.svg',
    navigateUrl: `/admin_ui/#/settings`
  },
  {
    key: '',
    name: 'Assay Configuration',
    localisationKey: 'assayConfiguration',
    icon: 'assets/Images/lobby/settings.svg',
    gridIcon: 'assets/Images/grid/settings.svg',
    navigateUrl: null
  }
];

export const DefaultIcons = [
  'view_order_on_dashboard',
  'view_workflow_on_dashboard',
  'view_nipt_on_dashboard',
  'view_connections_on_dashboard',
  'view_settings_on_dashboard'
];

export const getRoleBasedIcons = serverData => {
  const updatedCode = [];
  DefaultIcons.forEach(ele => {
    const data1 = serverData.find(ele1 => ele1 === ele);
    if (data1) {
    const data2 = Default_Icons_Info.find(ele2 => ele2.key === data1);
     if (data2) {
         updatedCode.push(data2);
     }
    }
 });
 return updatedCode;
};
