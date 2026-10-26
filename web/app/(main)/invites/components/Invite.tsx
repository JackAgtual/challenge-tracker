import User from "@/components/User";
import { components } from "@/types/api";
import {
  Card,
  CardAction,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { getChallengeNameAndDuration } from "@/lib/name-utils";
import { InvitedBy } from "./InvitedBy";
import { DeleteButton } from "./DeleteButton";
import { AcceptButton } from "./AcceptButton";
import { DeclineButton } from "./DeclineButton";

type Props = {
  invite: components["schemas"]["ReceivedInviteResponse"];
};

export function Invite({ invite }: Props) {
  const { id, challenge, inviteSender, canRespond } = invite;

  return (
    <Card key={id} className="w-full sm:w-fit">
      <CardHeader>
        <CardTitle>{getChallengeNameAndDuration(challenge)}</CardTitle>
        {!canRespond && (
          <CardAction>
            <DeleteButton inviteId={id} />
          </CardAction>
        )}
      </CardHeader>
      <CardContent>
        <InvitedBy user={inviteSender} />
        {!canRespond && (
          <p className="mt-2">
            Challenge started: {challenge.starDate ?? "Unknown"}
          </p>
        )}
      </CardContent>
      {canRespond && (
        <CardFooter className="grid grid-cols-2 w-fit gap-x-2">
          <AcceptButton inviteId={id} challengeId={challenge.id} />
          <DeclineButton inviteId={id} />
        </CardFooter>
      )}
    </Card>
  );
}
