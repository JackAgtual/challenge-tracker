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

type ParticipantStatusProps = {
  participants: components["schemas"]["ParticipantResponse"][];
  challengeId: number;
};

export default function ParticipantStatus({
  participants,
  challengeId,
}: ParticipantStatusProps) {
  return (
    <div>
      <Card>
        <CardHeader>
          <CardTitle>Participants</CardTitle>
          <CardDescription>
            <p>
              Participants that have accepted an invite to this challenge. Click
              on a user to see their goal definitions.
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
                href={`/challenges/${challengeId}/participants/${participant.id}/goals`}
              >
                {getParticipantDisplayName(participant)}
              </Link>
              <div>{participant.ready ? "Ready" : "Not ready"}</div>
            </div>
          ))}
        </CardContent>
      </Card>
    </div>
  );
}
