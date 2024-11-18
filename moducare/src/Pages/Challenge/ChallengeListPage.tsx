import * as React from 'react';
import {View, StyleSheet, ScrollView, Text} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import CustomText from '../../Components/Common/CustomText';
import SvgIconAtom from '../../Components/Common/SvgIconAtom';
import {colors} from '../../constants/colors';
import BigList from '../../Components/Challenge/BigList';
import {getChallengeList, getListType} from '../../api/challenge-api';
import {usePopup} from '../../hook/usePopup';
import PopupModal from '../../Components/Common/PopupModal';
import {useFocusEffect} from '@react-navigation/native';
import useChallenge from '../../hook/useChallenge';

export default function ChallengeListPage({navigation}) {
  const {visible, option, content, showPopup, hidePopup} = usePopup();
  // const [allList, setAllList] = React.useState<getListType[] | []>([]);
  const [page, setPage] = React.useState(10);

  // const getListCompo = async () => {
  //   const allData = await getChallengeList();
  //   setAllList(allData);
  // };
  // React.useEffect(() => {
  //   getListCompo();
  // }, []);

  const {AllLoading, getAllList} = useChallenge();

  useFocusEffect(
    React.useCallback(() => {
      getAllList.refetch();
    }, []),
  );

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.mainArea}>
        <View style={styles.topArea}>
          <SvgIconAtom name={'Challenge'} />
          <View>
            <CustomText size={20} label="챌린지를 통해" />
            <CustomText size={20} label="모두와 함께 두피를 지켜요." />
          </View>
        </View>
        <View style={styles.ListArea}>
          <ScrollView showsVerticalScrollIndicator={false}>
            {/* {allList.length !== 0 ? (
              allList.slice(0, page).map((data, index) => (
                <BigList
                  key={index}
                  title={data.challengeName}
                  user={data.challengeUser}
                  uri={data.challengeImg}
                  onPress={() =>
                    navigation.navigate('challenge_feed', {
                      id: data.challengeId,
                      title: data.challengeName,
                      type: 'allChallenge',
                    })
                  }
                />
              ))
            ) : (
              <View style={styles.nullList}>
                <CustomText label="개설된 챌린지가 없어요" size={18} />
              </View>
            )} */}
            {AllLoading ? (
              <View style={styles.nullList}>
                <CustomText label="불러오고 있어요!" size={18} />
              </View>
            ) : getAllList.data?.length !== 0 ? (
              getAllList.data?.slice(0, page).map((data, index) => (
                <BigList
                  key={index}
                  title={data.challengeName}
                  user={data.challengeUser}
                  uri={data.challengeImg}
                  onPress={() =>
                    navigation.navigate('challenge_feed', {
                      id: data.challengeId,
                      title: data.challengeName,
                      type: 'allChallenge',
                    })
                  }
                />
              ))
            ) : (
              <View style={styles.nullList}>
                <CustomText label="개설된 챌린지가 없어요" size={18} />
              </View>
            )}
            {getAllList.data?.length !== 0 && (
              // <Text style={styles.getListArea} onPress={handlePage}>
              //   더보기
              // </Text>
              <Text
                style={styles.getListArea}
                onPress={() => {
                  if (getAllList.data!.length >= page) {
                    setPage(page + 10);
                  } else {
                    showPopup({
                      option: 'Alert',
                      content: '마지막 페이지 입니다.',
                    });
                  }
                }}>
                더보기
              </Text>
            )}
          </ScrollView>
        </View>
      </View>
      <PopupModal
        visible={visible}
        onClose={hidePopup}
        content={content}
        option={option}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.WHITE,
    paddingHorizontal: 20,
  },
  mainArea: {
    flex: 1,
  },
  topArea: {
    gap: 10,
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignContent: 'center',
    marginBottom: 20,
  },
  ListArea: {
    flex: 1,
    backgroundColor: colors.WHITE,
    borderColor: colors.WHITE_GRAY,
    paddingTop: 20,
    borderWidth: 1,
    borderTopLeftRadius: 20,
    borderTopRightRadius: 20,
    elevation: 4,
  },
  getListArea: {
    paddingVertical: 20,
    textAlign: 'center',
    fontFamily: 'Pretendard-Bold',
    fontSize: 15,
    color: colors.DARK_GRAY,
  },
  nullList: {
    justifyContent: 'center',
    alignItems: 'center',
  },
});
