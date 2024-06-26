import * as React from 'react';
import { ActivityIndicator, ScrollView, Text, View } from 'react-native';
import humanizeDuration from 'humanize-duration';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useLocalSearchParams } from 'expo-router';
import { Item } from '@/app/components/Item';
import { useAppModel } from '@/global';
import { Theme } from '@/app/theme';

export default React.memo(() => {
    const safeArea = useSafeAreaInsets();
    let id = useLocalSearchParams().id as string;
    let appModel = useAppModel();
    let session = appModel.sessions.useFull(id);
    if (!session) {
        return (
            <View style={{ flexGrow: 1, flexBasis: 0, justifyContent: 'center', alignItems: 'center', backgroundColor: Theme.background }}>
                <ActivityIndicator size="large" color={Theme.accent} />
            </View>
        )
    }
    return (
        <ScrollView style={{ backgroundColor: Theme.background }} contentContainerStyle={{ paddingBottom: safeArea.bottom }}>
            <Item title={'Session #' + (session.index + 1)} />
            <Text style={{ color: Theme.text, paddingHorizontal: 16 }}>Status: {session.state}</Text>
            {session.classification ? <Text style={{ color: Theme.text, paddingHorizontal: 16 }}>Classification: {session.classification}</Text> : null}
            {session.audio ? <Text style={{ color: Theme.text, paddingHorizontal: 16 }}>Duration: {humanizeDuration(session.audio.duration, { units: ["h", "m", "s"] })}</Text> : null}
            {session.text && (
                <>
                    <Item title={'Transcription'} />
                    <Text style={{ color: Theme.text, paddingHorizontal: 16 }}>{session.text}</Text>
                </>
            )}
        </ScrollView>
    );
});