import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { components } from "@/types/api";
import Participant from "./Participant";
import ToggleReady from "./ToggleReady";

type BaseProps = {
  participants: components["schemas"]["ParticipantResponse"][];
  challengeId: number;
};

type PendingProps = BaseProps & {
  challengeIsPending: true;
  curUserIsOwner: boolean;
  curUserReadyResponse: components["schemas"]["ReadyResponse"];
};

type NonPendingProps = BaseProps & {
  challengeIsPending: false;
  curUserIsOwner?: never;
  curUserReadyResponse?: never;
};

type ParticipantsCardProps = PendingProps | NonPendingProps;

export default function ParticipantsCard({
  participants,
  challengeId,
  challengeIsPending,
  curUserIsOwner,
  curUserReadyResponse,
}: ParticipantsCardProps) {
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
          <div className="divide-y">
            {participants.map((participant) => (
              <Participant
                key={participant.id}
                participant={participant}
                challengeId={challengeId}
                challengeIsPending={challengeIsPending}
                curUserIsOwner={curUserIsOwner}
              />
            ))}
          </div>
        </CardContent>
        <CardFooter>
          {challengeIsPending && (
            <ToggleReady
              challengeId={challengeId}
              isReady={curUserReadyResponse.ready}
              eligibleToSetReady={curUserReadyResponse.eligibleToSetReady}
            />
          )}
        </CardFooter>
      </Card>
    </div>
  );
}
