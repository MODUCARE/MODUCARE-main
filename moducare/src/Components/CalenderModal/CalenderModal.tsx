import React, {useEffect, useState} from 'react';
import {
  Modal,
  StyleSheet,
  Dimensions,
  Pressable,
  TouchableOpacity,
  Text,
  View,
} from 'react-native';
import {colors} from '../../constants/colors';
import YearPicker from './YearPicker';
import CustomText from '../Common/CustomText';
import Feather from 'react-native-vector-icons/Feather';
import {getEncryptStorage} from '../../util/encryptedStorage';

interface CalenderModalProps {
  visible: boolean;
  onClose: () => void;
  children: React.ReactNode;
  onDateSelect: (date: string) => void;
  nowDate: string;
}

const HEIGHT = Dimensions.get('window').height;
const WIDTH = Dimensions.get('window').width;
const CalenderModal = ({
  visible,
  onClose,
  children,
  onDateSelect,
  nowDate,
}: CalenderModalProps) => {
  const months = [
    '01',
    '02',
    '03',
    '04',
    '05',
    '06',
    '07',
    '08',
    '09',
    '10',
    '11',
    '12',
  ];
  const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
  const [currentMonth, setCurrentMonth] = useState(new Date(nowDate));
  const [showYearPicker, setShowYearPicker] = useState(false);

  const goToNextMonth = () => {
    setCurrentMonth(
      new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1, 1),
    );
  };
  const goToPrevMonth = () => {
    setCurrentMonth(
      new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1, 1),
    );
  };

  // useEffect 추가
  useEffect(() => {
    const initializeDate = async () => {
      try {
        const info = await getEncryptStorage('info');
        if (info) {
          setCurrentMonth(new Date(info.birth)); // info.date가 날짜 문자열이라고 가정
        }
      } catch (error) {
        console.error('날짜 정보를 가져오는데 실패했습니다:', error);
      }
    };

    initializeDate();
  }, []);
  const generateMatrix = () => {
    const matrix: any[] = [];
    matrix[0] = daysOfWeek;

    const year = currentMonth.getFullYear();
    const month = currentMonth.getMonth();
    const firstDay = new Date(year, month, 1).getDay();
    const maxDays = new Date(year, month + 1, 0).getDate();

    var counter = -firstDay + 1;
    for (let row = 1; row < 7; row++) {
      matrix[row] = [];
      for (let col = 0; col < 7; col++) {
        if (counter <= 0) {
          const prevMonthDays = new Date(year, month, 0).getDate();
          const prevMonth = month - 1;
          const prevYear = prevMonth < 0 ? year - 1 : year;
          const adjustedMonth = (prevMonth + 12) % 12;

          matrix[row][col] = {
            day: prevMonthDays + counter,
            month: adjustedMonth,
            year: prevYear,
            isInCurrentMonth: false,
          };
        }
        // 현재 달의 날짜
        else if (counter <= maxDays) {
          matrix[row][col] = {
            day: counter,
            month: month,
            year: year,
            isInCurrentMonth: true,
          };
        }
        // 다음 달의 날짜
        else {
          const nextMonth = month + 1;
          const nextYear = nextMonth > 11 ? year + 1 : year;
          const adjustedMonth = nextMonth % 12;

          matrix[row][col] = {
            day: counter - maxDays,
            month: adjustedMonth,
            year: nextYear,
            isInCurrentMonth: false,
          };
        }
        counter++;
      }
    }
    return matrix;
  };

  const getTextStyle = (rowIndex: number, colIndex: number, item: any) => {
    if (rowIndex !== 0) {
      let textStyle = item.isInCurrentMonth // 현재 월에 포함되는 경우
        ? colIndex === 0
          ? styles.cellTextRed // 일요일
          : colIndex === 6
          ? styles.cellTextBlue // 토요일
          : styles.cellText // 평일
        : colIndex === 0
        ? {...styles.cellTextRed, ...styles.cellTextGrayOpacity} // 포함되지 않으면 회색 처리
        : colIndex === 6
        ? {...styles.cellTextBlue, ...styles.cellTextGrayOpacity}
        : {...styles.cellTextGray, ...styles.cellTextGrayOpacity};

      // 지금 찍혀있는 날짜와 같으면 selectedDay 스타일 적용
      const itemDate = `${item.year}-${String(item.month + 1).padStart(
        2,
        '0',
      )}-${String(item.day).padStart(2, '0')}`;
      if (itemDate === nowDate) {
        textStyle = styles.selectedDay;
      }
      return textStyle;
    } else {
      // 0번행은 요일 표시, 일 = 빨강, 토= 파랑, 평 = 검정
      return colIndex === 0
        ? styles.cellTextRed
        : colIndex === 6
        ? styles.cellTextBlue
        : styles.cellText;
    }
  };

  const renderCalendar = () => {
    const matrix = generateMatrix();
    const rows = matrix.map((row, rowIndex) => {
      const rowItems = row.map((item: any, colIndex: number) => {
        const textStyle = getTextStyle(rowIndex, colIndex, item);

        return (
          <TouchableOpacity
            style={styles.cell}
            key={colIndex}
            onPress={() =>
              handleDayPress(item.day, item.isInCurrentMonth, rowIndex)
            }>
            <Text style={textStyle}>
              {rowIndex === 0 ? daysOfWeek[colIndex] : item.day}
            </Text>
          </TouchableOpacity>
        );
      });
      return (
        <View style={styles.row} key={rowIndex}>
          {rowItems}
        </View>
      );
    });
    return <View style={styles.calender}>{rows}</View>;
  };

  const handleDayPress = (
    day: string,
    isInCurrentMonth: boolean,
    rowIndex: number,
  ) => {
    if (rowIndex === 0) {
      return;
    }
    const year = currentMonth.getFullYear();
    const month = currentMonth.getMonth();

    if (!isInCurrentMonth) {
      const isNextMonth = Number(day) < 15;
      const newMonth = isNextMonth ? month + 1 : month - 1;
      const newYear = newMonth < 0 ? year - 1 : newMonth > 11 ? year + 1 : year;

      const adjustMonth = (newMonth + 12) % 12;
      setCurrentMonth(new Date(newYear, adjustMonth, 1));

      const formattedMonth =
        adjustMonth < 9 ? `0${adjustMonth + 1}` : adjustMonth + 1;
      const formattedDay = Number(day) < 10 ? `0${day}` : day;
      const formattedDate = `${newYear}-${formattedMonth}-${formattedDay}`;

      onDateSelect(formattedDate);
    } else {
      const formattedMonth = month < 9 ? `0${month + 1}` : month + 1;
      const formattedDay = Number(day) < 10 ? `0${day}` : day;
      const formattedDate = `${year}-${formattedMonth}-${formattedDay}`;
      onDateSelect(formattedDate);
    }
  };

  return (
    <Modal
      visible={visible}
      transparent
      animationType="fade"
      onRequestClose={onClose}>
      <Pressable style={styles.overlay} onPress={onClose}>
        <Pressable style={styles.modalContainer}>
          <View style={styles.header}>
            <TouchableOpacity onPress={goToPrevMonth}>
              <Feather name="chevron-left" size={24} color={colors.BLACK} />
            </TouchableOpacity>
            <TouchableOpacity
              onPress={() => {
                setShowYearPicker(true);
              }}>
              <CustomText
                label={`${currentMonth.getFullYear()}.${
                  months[currentMonth.getMonth()]
                }`}
                variant="bold"
                size={24}
              />
            </TouchableOpacity>
            <TouchableOpacity onPress={goToNextMonth}>
              <Feather name="chevron-right" size={24} color={colors.BLACK} />
            </TouchableOpacity>
          </View>
          {showYearPicker && (
            <YearPicker
              setCurrentMonth={setCurrentMonth}
              setShowYearPicker={setShowYearPicker}
              currentMonth={currentMonth}
            />
          )}
          <View style={styles.calender}>{renderCalendar()}</View>
          {children}
        </Pressable>
      </Pressable>
    </Modal>
  );
};

export default CalenderModal;

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  modalContainer: {
    paddingTop: 20,
    width: WIDTH * 0.8,
    backgroundColor: colors.WHITE,
    borderRadius: 10,
    padding: 15,
    minHeight: HEIGHT * 0.5,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    margin: 20,
  },
  calender: {
    justifyContent: 'space-between',
  },
  monthLabel: {
    fontSize: 18,
    color: colors.BLACK,
  },
  row: {
    flexDirection: 'row',
  },
  cell: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    height: 40,
  },
  headerText: {
    color: colors.BLACK,
    fontSize: 18,
  },
  headerTextRed: {
    color: colors.NEGATIVE,
  },
  headerTextBlue: {
    color: colors.POSITIVE,
  },
  cellText: {
    color: colors.BLACK,
  },
  cellTextGray: {
    color: colors.GRAY,
  },
  cellTextRed: {
    color: colors.NEGATIVE,
  },
  cellTextBlue: {
    color: colors.POSITIVE,
  },
  selectedDay: {
    backgroundColor: colors.SUB,
    borderRadius: 20,
    textAlign: 'center',
    lineHeight: 40,
    color: colors.BLACK,
    overflow: 'hidden',
    width: 40,
    height: 40,
    opacity: 0.8,
  },
  cellTextGrayOpacity: {
    opacity: 0.3,
  },
});
