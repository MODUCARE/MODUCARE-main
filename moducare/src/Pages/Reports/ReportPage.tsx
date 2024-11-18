import {
  View,
  Image,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Pressable,
} from 'react-native';
import React, {useCallback, useState} from 'react';
import {colors} from '../../constants/colors';
import CustomText from '../../Components/Common/CustomText';
import ItemBox from '../../Components/ItemBox/ItemBox';
import {useReportQuery, ReportItem} from '../../quires/useReportsQuery';
import {Dimensions} from 'react-native';
import {useFocusEffect, useNavigation} from '@react-navigation/native';
import {RootStackParamList} from '../../navigate/StackNavigate';
import {NativeStackNavigationProp} from '@react-navigation/native-stack';
import {getEncryptStorage} from '../../util';

const HEIGHT = Dimensions.get('window').height;

export default function ReportPage() {
  const navigation =
    useNavigation<NativeStackNavigationProp<RootStackParamList>>();
  const {data: reportData, isLoading: reportLoading} = useReportQuery();
  const reportList = reportData;
  const [userName, setUserName] = useState('');

  const getInfo = async () => {
    try {
      const {name} = await getEncryptStorage('info');
      name && setUserName(name);
    } catch (error) {
      console.log(error);
    }
  };

  useFocusEffect(
    useCallback(() => {
      getInfo();
    }, []),
  );

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Image
          source={require('../../assets/img/report.png')}
          style={styles.reportIcon}
        />
        <View>
          <CustomText label={`${userName} 님의`} size={20} />
          <CustomText label="두피 리포트 입니다." size={20} />
        </View>
      </View>
      <ScrollView contentContainerStyle={styles.reportList}>
        {reportLoading && (
          <View style={styles.reportListEmpty}>
            <CustomText label="로딩중입니다..." />
          </View>
        )}
        {!reportList?.length && (
          <View style={styles.reportListEmpty}>
            <CustomText label="리포트가 아직 작성되지 않았습니다." />
          </View>
        )}
        {reportList &&
          reportList.map((report: ReportItem) => (
            <Pressable
              style={({pressed}) => [
                styles.buttonContainer,
                pressed && styles.pressed, // 누를 때 스타일
              ]}
              onPress={() => {
                navigation.navigate('aiResult', {
                  type: 'report',
                  id: report.id,
                  diagnosisResult: null,
                });
              }}>
              <ItemBox style={styles.reportCard}>
                <View style={styles.reportCardItem}>
                  <CustomText label="진단 일시" variant="regular" />
                  <CustomText label={report.date} variant="regular" />
                </View>
                <View style={styles.reportCardItem}>
                  <CustomText label="진단 결과" variant="regular" />
                  <CustomText label={report.diagnosis} variant="regular" />
                </View>
              </ItemBox>
            </Pressable>
          ))}
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.WHITE,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 20,
    margin: 20,
  },
  reportIcon: {
    width: 80,
    height: 100,
  },
  reportList: {
    margin: 20,
    gap: 12,
  },
  reportCard: {
    width: '100%',
  },
  reportCardItem: {
    flexDirection: 'row',
    gap: 20,
  },
  reportListEmpty: {
    height: HEIGHT * 0.5,
    alignItems: 'center',
    justifyContent: 'center',
  },
  pressed: {
    borderWidth: 3,
  },
  buttonContainer: {
    borderColor: colors.SUB,
    borderWidth: 1,
    borderRadius: 10,
    width: '100%',
  },
});
