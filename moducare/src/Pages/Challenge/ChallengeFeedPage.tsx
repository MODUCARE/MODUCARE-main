import React, {useState} from 'react';
import {FlatList, StyleSheet, Text, View} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {colors} from '../../constants/colors';
import FeedNav from '../../Components/Common/FeedNav';
import FeedJoinNav from '../../Components/Common/FeedJoinNav';
import FeedItem from '../../Components/Challenge/FeedItem';
import {postJoinChallenge, postOutChallenge} from '../../api/challenge-api';
import CustomText from '../../Components/Common/CustomText';
import {
  RouteProp,
  useFocusEffect,
  useNavigation,
} from '@react-navigation/native';
import {getEncryptStorage} from '../../util';
import {NativeStackNavigationProp} from '@react-navigation/native-stack';
import {RootStackParamList} from '../../navigate/StackNavigate';
import {usePopup} from '../../hook/usePopup';
import PopupModal from '../../Components/Common/PopupModal';
import useGetFeeds from '../../hook/useGetFeeds';

const ChallengeFeedPage = ({
  route,
}: {
  route: RouteProp<RootStackParamList, 'challenge_feed'>;
}) => {
  const navigation =
    useNavigation<NativeStackNavigationProp<RootStackParamList>>();
  const {id, title, type} = route.params;
  const [isType, setIsType] = useState(type);
  const {
    visible,
    option,
    content,
    showPopup,
    hidePopup,
    popupConfirm,
    popupCancel,
  } = usePopup();

  const handleWrite = async () => {
    const isDone = await getEncryptStorage('isDone');
    if (isDone === 0) {
      navigation.navigate('challenge_write', {id});
    } else {
      showPopup({
        option: 'Alert',
        content: '오늘 인증 완료! \n내일 다시 해주세요!',
        confirm: () => {
          hidePopup();
        },
      });
    }
  };

  const handleExit = async () => {
    await postOutChallenge(id);
    navigation.goBack();
  };
  const handleJoin = async () => {
    showPopup({option: 'Loading', content: '챌린지 추가 중 입니다.'});
    await postJoinChallenge(id);
    showPopup({
      option: 'confirmMark',
      content: '챌린지 추가 완료!',
      confirm: () => navigation.goBack(),
    });
  };

  const {
    data: posts,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
    refetch,
  } = useGetFeeds(id);
  const [isRefreshing, setIsRefreshing] = useState(false);

  const handleRefresh = async () => {
    setIsRefreshing(true);
    await refetch();
    setIsRefreshing(false);
  };

  const handleEndReached = () => {
    if (hasNextPage && !isFetchingNextPage) {
      fetchNextPage();
    }
  };

  useFocusEffect(
    React.useCallback(() => {
      refetch();
    }, []),
  );

  return (
    <SafeAreaView style={styles.container}>
      <Text
        style={[
          styles.text,
          {fontFamily: 'Pretendard-Bold', color: colors.BLACK},
        ]}>
        {title}
      </Text>
      <Text
        style={[
          styles.text,
          {fontFamily: 'Pretendard-Regular', color: colors.DARK_GRAY},
        ]}>
        다른 사람들과 함께 루틴을 지켜봐요!
      </Text>
      {isType === 'myChallenge' ? (
        <FeedNav onPress={handleWrite} onExit={handleExit} />
      ) : (
        <FeedJoinNav onJoin={handleJoin} />
      )}
      {posts?.pages.flat().length !== 0 ? (
        <FlatList
          data={posts?.pages.flat()}
          renderItem={({item}) => (
            <FeedItem
              feedId={item.feedId}
              feedUserName={item.feedUserName}
              content={item.content}
              feedRegDate={item.feedRegDate}
              uri={item.feedImg}
              like={item.like}
              isLiked={item.isLiked}
            />
          )}
          keyExtractor={item => String(item.feedId)}
          numColumns={1}
          contentContainerStyle={styles.FeedList}
          onEndReached={handleEndReached}
          onEndReachedThreshold={2}
          refreshing={isRefreshing}
          onRefresh={handleRefresh}
          scrollIndicatorInsets={{right: 1}}
          indicatorStyle="black"
        />
      ) : (
        <View style={styles.nullList}>
          <CustomText label="인증 피드가 존재하지 않아요" size={18} />
          <CustomText
            label="다른 사람들보다 먼저 인증피드를 올려보세요!"
            size={18}
          />
        </View>
      )}
      <PopupModal
        visible={visible}
        option={option}
        onClose={hidePopup}
        content={content}
        confirm={
          // option === 'confirmMark'
          //   ? () => {
          //       navigation.goBack();
          //     }
          //   : popupConfirm
          popupConfirm
        }
        cancel={popupCancel}
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.WHITE,
  },
  text: {
    textAlign: 'center',
    fontSize: 20,
  },
  FeedList: {
    padding: 5,
  },
  nullList: {
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default ChallengeFeedPage;
