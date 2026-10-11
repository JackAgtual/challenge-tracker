import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { getParticipantDisplayName } from "@/lib/participant-utils";
import { components } from "@/types/api";
import Link from "next/link";
import RemoveParticipantButton from "./pending-challenges/RemoveParticipantButton";

type ParticipantStatusProps = {
  participants: components["schemas"]["ParticipantResponse"][];
  challengeId: number;
  challengeIsPending: boolean;
  curUserIsOwner: boolean;
};

export default function ParticipantStatus({
  participants,
  challengeId,
  challengeIsPending,
  curUserIsOwner,
}: ParticipantStatusProps) {
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
                {getParticipantDisplayName(participant)}
              </Link>
              {challengeIsPending && (
                <div className="flex gap-x-2">
                  <div className="flex items-center">
                    {participant.ready ? "Ready" : "Not ready"}
                  </div>
                  {curUserIsOwner && (
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
      </Card>
    </div>
  );
}
