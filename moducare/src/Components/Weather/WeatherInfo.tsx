import React from 'react';
import {View, StyleSheet, Platform, PermissionsAndroid} from 'react-native';
import ItemBox from '../ItemBox/ItemBox';
import CustomText from '../Common/CustomText';
import SvgIconAtom from '../Common/SvgIconAtom';
import {handleAirPhrase, handleUvPhrase} from '../../store/useWheaterStore';
import Geolocation from 'react-native-geolocation-service';
import {getLocation} from '../../api/weather-api';
import {useWeatherQuery} from '../../quires/useWheaterQuery';

export default function WeatherInfo() {
  const [sido, setSido] = React.useState<string>('');
  const [gugun, setGugun] = React.useState<string>('');
  // const [weatherData, setWeatherData] = React.useState<
  //   WeatherDataProps | undefined
  // >(undefined);
  const {data: weatherData, isLoading} = useWeatherQuery({
    sido,
    gugun,
  });

  React.useEffect(() => {
    if (Platform.OS === 'android') {
      PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
      );
    }
  }, []);

  React.useEffect(() => {
    const watchId = Geolocation.watchPosition(
      position => {
        const {latitude, longitude} = position.coords;
        getLocation(latitude, longitude).then(location => {
          setSido(location.region_1depth_name.slice(0, 2));
          setGugun(location.region_2depth_name);
        });
      },
      error => {
        console.log(error);
      },
      {
        enableHighAccuracy: true,
        distanceFilter: 1,
      },
    );

    return () => {
      Geolocation.clearWatch(watchId);
    };
  }, [sido, gugun]);

  console.log('weatherData', weatherData);
  return (
    <View>
      <ItemBox>
        <CustomText label="오늘의 날씨 정보" size={20} />
        <View style={styles.weatherBox}>
          <View style={styles.weatherBoxItem}>
            <SvgIconAtom name="Main_temparature" size={70} />
            <CustomText
              label={`${weatherData?.weatherDto?.temperature || ''}℃`}
              size={20}
            />
          </View>
          <View style={styles.weatherBoxItem}>
            <SvgIconAtom name="Main_air" size={70} />
            <CustomText
              label={handleAirPhrase(weatherData?.weatherDto?.airCondition)}
              size={20}
            />
          </View>
          <View style={styles.weatherBoxItem}>
            <SvgIconAtom name="Main_UV" size={70} />
            <CustomText
              label={handleUvPhrase(weatherData?.weatherDto?.uvCondition)}
              size={20}
            />
          </View>
        </View>
        <View style={styles.gptResponse}>
          <CustomText
            label={weatherData?.gptResponse || '로딩 중...'}
            size={16}
            variant="regular"
          />
        </View>
      </ItemBox>
    </View>
  );
}

const styles = StyleSheet.create({
  weatherBoxImage: {
    width: 70,
    height: 70,
  },
  weatherBox: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    margin: 20,
  },
  weatherBoxItem: {
    alignItems: 'center',
    gap: 5,
  },
  gptResponse: {
    margin: 20,
  },
});