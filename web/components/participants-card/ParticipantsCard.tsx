import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { components } from "@/types/api";
import { Participant } from "./Participant";
import { RemoveParticipantButton } from "./RemoveParticipantButton";
import { ToggleReady } from "./ToggleReady";

type BaseProps = {
  participants: components["schemas"]["ParticipantResponse"][];
  challengeId: number;
  selectedParticipantId: number;
};

type PendingProps = BaseProps & {
  challengeIsPending: true;
  curParticipantId: number;
  curUserIsOwner: boolean;
  curUserReadyResponse: components["schemas"]["ReadyResponse"];
};

type NonPendingProps = BaseProps & {
  challengeIsPending: false;
  curParticipantId?: never;
  curUserIsOwner?: never;
  curUserReadyResponse?: never;
};

type Props = PendingProps | NonPendingProps;

export function ParticipantsCard({
  participants,
  challengeId,
  challengeIsPending,
  curParticipantId,
  curUserIsOwner,
  curUserReadyResponse,
  selectedParticipantId,
}: Props) {
  const participantsReadyCount = participants.filter((p) => p.ready).length;
  const totalParticipantsCount = participants.length;

  return (
    <div>
      <Card>
        <CardHeader>
          <CardTitle>Participants</CardTitle>
          <CardDescription>
            {challengeIsPending ? (
              <div className="flex flex-col gap-y-2">
                <p>
                  All users participating in this challenge. Click on a user to
                  see their goal definitions.
                </p>
                <p>
                  Once you are content with your goal definitions click the
                  Ready button. The owner can't start the challenge until all
                  participants are ready.
                </p>
              </div>
            ) : (
              <p>
                Participants of this challenge. Click on a user to see their
                goal completions.
              </p>
            )}
          </CardDescription>
        </CardHeader>
        <CardContent>
          {challengeIsPending && (
            <p className="text-right">{`${participantsReadyCount} / ${totalParticipantsCount} participants ready`}</p>
          )}
          <div className="grid grid-flow-col auto-cols-fr gap-x-4 w-fit">
            {participants.map((participant) => (
              <Participant
                key={participant.id}
                participant={participant}
                challengeIsPending={challengeIsPending}
                selected={participant.id === selectedParticipantId}
              />
            ))}
          </div>
        </CardContent>
        <CardFooter className="flex gap-x-2">
          {challengeIsPending && (
            <>
              <ToggleReady
                challengeId={challengeId}
                isReady={curUserReadyResponse.ready}
                eligibleToSetReady={curUserReadyResponse.eligibleToSetReady}
              />
              {curUserIsOwner && (
                <RemoveParticipantButton
                  challengeId={challengeId}
                  participantId={selectedParticipantId}
                  disabled={curParticipantId === selectedParticipantId}
                />
              )}
            </>
          )}
        </CardFooter>
      </Card>
    </div>
  );
}
