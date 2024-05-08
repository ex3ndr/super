import { useRoute } from '@react-navigation/native';
import * as React from 'react';
import { Pressable, ScrollView, Text, View } from 'react-native';
import { Image } from 'expo-image';
import { Memory } from '../../modules/api/schema';
import { Theme } from '../../theme';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useRouter } from '../../routing';

export const MemoryScreen = React.memo(() => {
    const safeArea = useSafeAreaInsets();
    const router = useRouter();
    let memory = (useRoute().params as any).data as Memory;
    return (
        <>
            <ScrollView style={{ flexGrow: 1, backgroundColor: Theme.background }} contentContainerStyle={{ paddingBottom: safeArea.bottom + 64 + 32 }}>
                {memory.image && memory.imageMetadata && <Image style={{ height: 'auto', aspectRatio: memory.imageMetadata.width / memory.imageMetadata.height, borderBottomLeftRadius: 16, borderBottomRightRadius: 16 }} source={{ uri: memory.image }} placeholder={{ thumbhash: memory.imageMetadata.thumbhash }} />}
                {!memory.image && <View style={{ height: 16 }} />}
                <View style={{ flexDirection: 'column', flexGrow: 1, flexBasis: 0, paddingTop: 16, paddingHorizontal: 16 }}>
                    <Text style={{ fontSize: 24, marginBottom: 16, color: Theme.text }}>{memory.title}</Text>
                    <Text style={{ fontSize: 16, opacity: 0.7, color: Theme.text }}>{memory.summary}</Text>
                </View>
            </ScrollView>
            {/* <View style={{ position: 'absolute', bottom: safeArea.bottom, left: 0, right: 0, alignItems: 'stretch' }}>
                <Pressable style={{ height: 48, borderRadius: 32, backgroundColor: '#222222', justifyContent: 'center', alignItems: 'center', marginHorizontal: 16, marginVertical: 16 }} onPress={() => router.navigate('discussion')}>
                    <Text style={{ color: Theme.text, fontSize: 16 }}>Chat with AI</Text>
                </Pressable>
            </View> */}
        </>
    );
});