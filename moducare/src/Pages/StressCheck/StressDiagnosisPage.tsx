import * as React from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Dimensions,
} from 'react-native';
import {stressQuestion, stressAnswer} from './StressQuestion';
import {useRef, useState} from 'react';
import CustomButton from '../../Components/Common/CustomButton';
import CustomText from '../../Components/Common/CustomText';
import {colors} from '../../constants/colors';
import AntDesign from 'react-native-vector-icons/AntDesign';
import {postResult} from '../../api/stress-check-api';

import {NavigationProp, useNavigation} from '@react-navigation/native';
import {Stack} from '../../util/stack';
const WIDTH = Dimensions.get('window').width;

type RootStackParamList = {
  StressResultPage: {stressScore: number};
};

export default function StressDiagnosisPage() {
  const scoreStack = useRef(new Stack());

  const navigation = useNavigation<NavigationProp<RootStackParamList>>();
  const [isChecked, setIsChecked] = useState(-1);
  const [QuestionIdx, setQuestionIdx] = useState(0);
  const [stressScore, setStressScore] = useState(0);

  const handleNext = () => {
    if (isChecked !== -1) {
      if (QuestionIdx === 14) {
        if (postResult(stressScore + isChecked) !== undefined) {
          navigation.navigate('StressResultPage', {
            stressScore: stressScore + isChecked, // 마지막 질문의 점수도 포함
          });
        }
      } else {
        setStressScore(stressScore + isChecked);
        setQuestionIdx(QuestionIdx + 1);
        setIsChecked(-1);
        scoreStack.current.push(isChecked);
      }
    } else {
      return;
    }
  };

  const handlePrev = () => {
    if (!scoreStack.current.isEmpty()) {
      let prevScore = scoreStack.current.pop();
      console.log(QuestionIdx, prevScore, stressScore);
      setStressScore(stressScore - prevScore);
      setQuestionIdx(QuestionIdx - 1);
      // setIsChecked(-1);
      setIsChecked(prevScore);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.questionContainer}>
        <CustomText
          label={stressQuestion[QuestionIdx].question}
          size={20}
          variant="regular"
        />
      </View>

      <View style={styles.answerContainer}>
        {stressAnswer.map(answer => (
          <TouchableOpacity
            key={answer.id}
            onPress={() => setIsChecked(answer.id)}
            style={[
              styles.answer,
              isChecked === answer.id && styles.selectedAnswerContainer,
            ]}>
            <AntDesign
              name={isChecked === answer.id ? 'checksquare' : 'checksquareo'}
              size={24}
              color={isChecked === answer.id ? colors.MAIN : colors.GRAY}
            />
            <Text
              style={[
                styles.answerText,
                isChecked === answer.id && styles.selectedAnswerText,
              ]}>
              {answer.answer}
            </Text>
          </TouchableOpacity>
        ))}
      </View>

      <View style={styles.bottomContainer}>
        <View style={styles.progressContainer}>
          <View style={styles.progressLine} />
          <View
            style={[
              styles.progressBar,
              {width: `${(QuestionIdx / 15) * 100}%`},
            ]}
          />
        </View>
        <View style={styles.buttonContainer}>
          <CustomButton
            label="이전"
            size="small"
            variant="filled"
            onPress={() => handlePrev()}
          />
          <CustomButton
            label="다음"
            size="small"
            variant="filled"
            onPress={() => handleNext()}
          />
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#fff',
  },
  buttonContainer: {
    flexDirection: 'row',
    width: '100%',
  },
  questionContainer: {
    paddingVertical: 40,
  },
  answerContainer: {
    gap: 20,
  },
  answer: {
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderWidth: 1,
    borderRadius: 10,
    width: WIDTH * 0.8,
    height: 50,
    borderColor: colors.GRAY,
    flexDirection: 'row',
    gap: 20,
  },
  answerText: {
    fontSize: 16,
  },
  selectedAnswerContainer: {
    borderWidth: 2,
    borderColor: colors.MAIN,
  },
  selectedAnswerText: {
    color: colors.MAIN,
    fontWeight: 'bold',
  },
  progressContainer: {
    alignSelf: 'center',
    marginBottom: 20,
  },
  progressLine: {
    width: WIDTH * 0.9,
    height: 6,
    backgroundColor: colors.LIGHT_GRAY,
    borderRadius: 10,
  },
  progressBar: {
    height: 6,
    backgroundColor: colors.MAIN,
    borderRadius: 10,
    alignSelf: 'flex-start',
    position: 'absolute',
  },
  bottomContainer: {
    position: 'absolute',
    bottom: 50,
  },
});
