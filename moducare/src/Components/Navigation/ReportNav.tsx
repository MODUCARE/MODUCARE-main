import {createMaterialTopTabNavigator} from '@react-navigation/material-top-tabs';
import React from 'react';
import ReportPage from '../../Pages/Reports/ReportPage';
import DiaryPage from '../../Pages/Reports/DiaryPage';
import {colors} from '../../constants/colors';
import {Dimensions} from 'react-native';

const Tab = createMaterialTopTabNavigator();
const WIDTH = Dimensions.get('window').width;
const HEIGHT = Dimensions.get('window').height;
export default function ReportNav() {
  return (
    <Tab.Navigator
      screenOptions={{
        tabBarLabelStyle: {
          fontFamily: 'Pretendard-Bold',
          fontSize: 20,
          fontWeight: 'bold',
        },
        tabBarPressColor: colors.WHITE,
        tabBarItemStyle: {width: WIDTH / 2},
        tabBarIndicatorStyle: {backgroundColor: colors.MAIN},
        tabBarActiveTintColor: colors.BLACK,
        swipeEnabled: false,
        tabBarStyle: {
          height: HEIGHT * 0.065,
          borderTopWidth: 0,
          shadowColor: 'transparent',
          backgroundColor: colors.WHITE,
        },
      }}>
      <Tab.Screen name="두피 리포트" component={ReportPage} />
      <Tab.Screen name="두피 다이어리" component={DiaryPage} />
    </Tab.Navigator>
  );
}
