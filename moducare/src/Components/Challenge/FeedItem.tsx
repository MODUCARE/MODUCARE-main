import React, {useState} from 'react';
import {Image, StyleSheet, Text, View} from 'react-native';
import CustomText from '../Common/CustomText';
import {colors} from '../../constants/colors';
import AntDesign from 'react-native-vector-icons/AntDesign';
import {postLike} from '../../api/challenge-api';

interface FeedType {
  feedId: number;
  uri: string;
  feedUserName: string;
  content: string;
  feedRegDate: string;
  like: number;
  isLiked: number;
}
const FeedItem = ({
  feedId,
  uri,
  feedUserName,
  content,
  feedRegDate,
  like,
  isLiked,
}: FeedType) => {
  const [love, setLove] = useState(isLiked);
  const [count, setCount] = useState(like);

  const handleLike = async () => {
    await postLike(feedId, 0);
    setCount(count + 1);
    setLove(1);
  };

  const handleUnLike = async () => {
    await postLike(feedId, 1);
    setCount(count - 1);
    setLove(0);
  };
  return (
    <View style={styles.container}>
      <CustomText label={feedUserName} size={20} />
      <Text style={styles.dateText}>{feedRegDate}</Text>
      <Image style={styles.imgArea} source={{uri}} />
      <Text style={styles.Content}>{content}</Text>
      <View style={styles.likeArea}>
        {love === 0 ? (
          <AntDesign
            name="hearto"
            size={25}
            color={'red'}
            onPress={handleLike}
          />
        ) : (
          <AntDesign
            name="heart"
            size={25}
            color={'red'}
            onPress={handleUnLike}
          />
        )}
        <CustomText label={count.toString()} size={20} variant="regular" />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    backgroundColor: colors.WHITE,
    borderRadius: 20,
    marginHorizontal: 20,
    padding: 20,
    elevation: 4,
    marginBottom: 20,
  },
  dateText: {
    fontFamily: 'Pretendard-Medium',
    fontSize: 12,
    textAlign: 'right',
    marginBottom: 10,
  },
  imgArea: {
    width: '100%',
    height: 300,
    marginHorizontal: 'auto',
    resizeMode: 'cover',
    borderRadius: 15,
  },
  Content: {
    fontFamily: 'Pretendard-Bold',
    fontSize: 15,
    color: colors.DARK_GRAY,
    marginVertical: 10,
  },
  likeArea: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    alignItems: 'center',
    gap: 10,
  },
});

export default FeedItem;
