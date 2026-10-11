import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { getDisplayNameFromParticipant } from "@/lib/name-utils";
import { components } from "@/types/api";
import Link from "next/link";
import RemoveParticipantButton from "./RemoveParticipantButton";
import ToggleReady from "./ToggleReady";

type BaseProps = {
  participants: components["schemas"]["ParticipantResponse"][];
  challengeId: number;
};

type PendingProps = BaseProps & {
  challengeIsPending: true;
  curUserIsOwner: boolean;
  curUserIsReady: boolean;
};

type NonPendingProps = BaseProps & {
  challengeIsPending: false;
  curUserIsOwner?: never;
  curUserIsReady?: never;
};

type ParticipantsCardProps = PendingProps | NonPendingProps;

export default function ParticipantsCard({
  participants,
  challengeId,
  challengeIsPending,
  curUserIsOwner,
  curUserIsReady,
}: ParticipantsCardProps) {
  return (
    <div>
      <Card>
        <CardHeader>
          <CardTitle>Participants</CardTitle>
          <CardDescription>
            <p>
              {challengeIsPending
                ? "Participants that have accepted an invite to this challenge. Click on a user to see their goal definitions."
                : "Participants of this challenge. Click on a user to see their goal completions."}
            </p>
          </CardDescription>
        </CardHeader>
        <CardContent className="divide-y">
          {participants.map((participant) => (
            <div
              key={participant.id}
              className="flex justify-between py-2 my-0"
            >
              <Link
                className="flex items-center"
                href={`/challenges/${challengeId}/participants/${participant.id}/goals/${challengeIsPending ? "" : "/completions"}`}
              >
                {getDisplayNameFromParticipant(participant)}
              </Link>
              {challengeIsPending && (
                <div className="flex gap-x-2">
                  <div className="flex items-center">
                    {participant.ready ? "Ready" : "Not ready"}
                  </div>
                  {curUserIsOwner && !participant.isChallengeOwner && (
                    <RemoveParticipantButton
                      challengeId={challengeId}
                      participantId={participant.id}
                    />
                  )}
                </div>
              )}
            </div>
          ))}
        </CardContent>
        <CardFooter>
          {challengeIsPending && (
            <ToggleReady challengeId={challengeId} isReady={curUserIsReady} />
          )}
        </CardFooter>
      </Card>
    </div>
  );
}
